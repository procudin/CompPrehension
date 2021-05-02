package org.vstu.compprehension.Service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vstu.compprehension.dto.ExerciseAttemptDto;
import org.vstu.compprehension.dto.ExerciseStatisticsItemDto;
import org.vstu.compprehension.dto.FeedbackDto;
import org.vstu.compprehension.dto.InteractionDto;
import org.vstu.compprehension.dto.question.QuestionDto;
import org.vstu.compprehension.models.businesslogic.Question;
import org.vstu.compprehension.models.businesslogic.Strategy;
import org.vstu.compprehension.models.entities.EnumData.AttemptStatus;
import org.vstu.compprehension.models.entities.ExerciseAttemptEntity;
import org.vstu.compprehension.models.entities.InteractionEntity;
import org.vstu.compprehension.models.entities.QuestionEntity;
import org.vstu.compprehension.models.repository.ExerciseAttemptRepository;
import org.vstu.compprehension.models.repository.FeedbackRepository;
import org.vstu.compprehension.utils.Mapper;

import java.util.ArrayList;
import java.util.Arrays;

import static org.vstu.compprehension.models.entities.EnumData.InteractionType.REQUEST_CORRECT_ANSWER;
import static org.vstu.compprehension.models.entities.EnumData.InteractionType.SEND_RESPONSE;

@Service
public class FrontendService {
    @Autowired
    private ExerciseAttemptRepository exerciseAttemptRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private Strategy strategy;

    @Autowired
    private FeedbackRepository feedbackRepository;


    public FeedbackDto addQuestionAnswer(InteractionDto interaction) throws Exception {
        Long exAttemptId = interaction.getAttemptId();
        Long questionId = interaction.getQuestionId();
        Long[][] answers = interaction.getAnswers();

        ExerciseAttemptEntity attempt = exerciseAttemptRepository.findById(exAttemptId)
                .orElseThrow(() -> new Exception("Can't find attempt with id " + exAttemptId));

        // evaluate answer
        val tags = attempt.getExercise().getTags();
        val question = questionService.getSolvedQuestion(questionId);
        val responses = questionService.responseQuestion(question, answers);
        val judgeResult = questionService.judgeQuestion(question, responses, tags);
        val explanations = questionService.explainViolations(question, judgeResult.violations);
        val errors = explanations.stream().map(s -> s.getText()).toArray(String[]::new);

        // add interaction
        val existingInteractions = question.getQuestionData().getInteractions();
        val ie = new InteractionEntity(SEND_RESPONSE, question.getQuestionData(), judgeResult.violations, judgeResult.correctlyAppliedLaws, responses);
        existingInteractions.add(ie);
        val correctInteractionsCount = (int)existingInteractions.stream().filter(i -> i.getViolations().size() == 0).count();

        // add feedback
        val grade = strategy.grade(attempt);
        ie.getFeedback().setInteractionsLeft(judgeResult.IterationsLeft);
        ie.getFeedback().setGrade(grade);
        feedbackRepository.save(ie.getFeedback());

        // remove incorrect answers from feedback
        val correctAnswers = errors.length > 0
                ? Arrays.copyOf(answers, answers.length - 1)
                : answers;

        return FeedbackDto.builder()
                .grade(grade)
                .errors(errors)
                .stepsLeft(judgeResult.IterationsLeft)
                .correctSteps(correctInteractionsCount)
                .stepsWithErrors((int)existingInteractions.stream().filter(i -> i.getViolations().size() > 0).count())
                .correctAnswers(correctAnswers)
                .build();
    }

    public QuestionDto generateQuestion(Long exAttemptId) throws Exception {
        ExerciseAttemptEntity attempt = exerciseAttemptRepository.findById(exAttemptId)
                .orElseThrow(() -> new Exception("Can't find attempt with id " + exAttemptId));
        Question question = questionService.generateQuestion(attempt);
        QuestionEntity qData = question.getQuestionData();
        return Mapper.toDto(qData);
    }

    public QuestionDto getQuestion(Long questionId) throws Exception {
        val question = questionService.getQuestionEntiity(questionId);
        return Mapper.toDto(question);
    }

    public FeedbackDto generateNextCorrectAnswer(Long questionId) throws Exception {
        // get next correct answer
        val question = questionService.getSolvedQuestion(questionId);
        val correctAnswer = questionService.getNextCorrectAnswer(question);
        val correctAnswerDto = Mapper.toDto(correctAnswer);

        // evaluate new answer
        val tags = question.getQuestionData().getExerciseAttempt().getExercise().getTags();
        val responses = questionService.responseQuestion(question, correctAnswerDto.getAnswers());
        val judgeResult = questionService.judgeQuestion(question, responses, tags);

        // add interaction
        val existingInteractions = question.getQuestionData().getInteractions();
        val ie = new InteractionEntity(REQUEST_CORRECT_ANSWER, question.getQuestionData(), judgeResult.violations, judgeResult.correctlyAppliedLaws, responses);
        existingInteractions.add(ie);
        val correctInteractionsCount = (int)existingInteractions.stream().filter(i -> i.getViolations().size() == 0).count();

        // add feedback
        val grade = strategy.grade(question.getQuestionData().getExerciseAttempt());
        ie.getFeedback().setInteractionsLeft(judgeResult.IterationsLeft);
        ie.getFeedback().setGrade(grade);
        feedbackRepository.save(ie.getFeedback());

        return FeedbackDto.builder()
                .grade(grade)
                .errors(new String[0])
                .explanation(correctAnswerDto.getExplanation())
                .correctAnswers(correctAnswerDto.getAnswers())
                .stepsLeft(judgeResult.IterationsLeft)
                .correctSteps(correctInteractionsCount)
                .stepsWithErrors((int)existingInteractions.stream().filter(i -> i.getViolations().size() > 0).count())
                .build();
    }

    public ExerciseStatisticsItemDto[] getExerciseStatistics(Long exerciseId) {
        val exercise = exerciseService.getExercise(exerciseId);

        val result = exercise.getExerciseAttempts().stream()
                .map(att -> {
                    val questionsCount = att.getQuestions().size();
                    val totalInteractionsCount = att.getQuestions().stream()
                            .filter(q -> q.getInteractions() != null)
                            .flatMap(q -> q.getInteractions().stream()).count();
                    val totalInteractionsWithErrorsCount = att.getQuestions().stream()
                            .filter(q -> q.getInteractions() != null)
                            .flatMap(q -> q.getInteractions().stream())
                            .filter(i -> i.getViolations().size() > 0).count();
                    double avgGrade = att.getQuestions().stream()
                            .filter(q -> q.getInteractions() != null && q.getInteractions().size() > 0)
                            .mapToDouble(q -> {
                                val last = q.getInteractions().stream().reduce((f, s) -> s);
                                val grade = last.map(l -> l.getFeedback()).map(f -> f.getGrade()).orElse(0f);
                                return grade;
                            })
                            .average()
                            .orElse(0d);

                    return ExerciseStatisticsItemDto.builder()
                            .attemptId(att.getId())
                            .averageGrade(avgGrade)
                            .questionsCount(questionsCount)
                            .totalInteractionsCount((int)totalInteractionsCount)
                            .totalInteractionsWithErrorsCount((int)totalInteractionsWithErrorsCount)
                            .build();
                })
                .toArray(ExerciseStatisticsItemDto[]::new);

        return result;
    }

    public ExerciseAttemptDto getExistingExerciseAttempt(Long exerciseId, Long userId) throws Exception {
        val existingAttempt = exerciseAttemptRepository
                .findFirstByExerciseIdAndUserIdAndAttemptStatusOrderByIdDesc(exerciseId, userId, AttemptStatus.INCOMPLETE);
        return existingAttempt
                .map(Mapper::toDto)
                .orElse(null);
    }

    public ExerciseAttemptDto createExerciseAttempt(Long exerciseId, Long userId) throws Exception {
        val exercise = exerciseService.getExercise(exerciseId);
        val user = userService.getUser(userId);

        val ea = new ExerciseAttemptEntity();
        ea.setExercise(exercise);
        ea.setUser(user);
        ea.setAttemptStatus(AttemptStatus.INCOMPLETE);
        ea.setQuestions(new ArrayList<>(0));
        exerciseAttemptRepository.save(ea);

        return Mapper.toDto(ea);
    }
}