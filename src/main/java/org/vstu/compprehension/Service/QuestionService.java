package org.vstu.compprehension.Service;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vstu.compprehension.models.businesslogic.*;
import org.vstu.compprehension.models.businesslogic.backend.Backend;
import org.vstu.compprehension.models.businesslogic.domains.Domain;
import org.vstu.compprehension.models.entities.*;
import org.vstu.compprehension.models.entities.EnumData.FeedbackType;
import org.vstu.compprehension.models.repository.AnswerObjectRepository;
import org.vstu.compprehension.models.repository.BackendFactRepository;
import org.vstu.compprehension.models.repository.QuestionRepository;
import org.vstu.compprehension.models.entities.EnumData.Language;
import org.vstu.compprehension.models.entities.EnumData.QuestionType;
import org.vstu.compprehension.models.repository.ResponseRepository;
import org.vstu.compprehension.utils.DomainAdapter;
import org.vstu.compprehension.utils.HyperText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Service
public class QuestionService {
    private Core core = new Core();

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    AnswerObjectRepository answerObjectRepository;

    @Autowired
    BackendFactRepository backendFactRepository;

    @Autowired
    private Strategy strategy;

    @Autowired
    private Backend backend;

    @Autowired
    private DomainService domainService;

    @Autowired
    ResponseRepository responseRepository;

    public Question generateQuestion(ExerciseAttemptEntity exerciseAttempt) {
        Domain domain = DomainAdapter.getDomain(exerciseAttempt.getExercise().getDomain().getName());
        QuestionRequest qr = strategy.generateQuestionRequest(exerciseAttempt);
        Question question = domain.makeQuestion(qr, exerciseAttempt.getExercise().getTags(), exerciseAttempt.getUser().getPreferred_language());
        question.getQuestionData().setDomainEntity(domainService.getDomainEntity(domain.getName()));
        saveQuestion(question.getQuestionData());
        return question;
    }

    public @Nullable Question generateSupplementaryQuestion(@NotNull QuestionEntity sourceQuestion, @NotNull ViolationEntity violation) {
        val domain = DomainAdapter.getDomain(sourceQuestion.getExerciseAttempt().getExercise().getDomain().getName());
        val question = domain.makeSupplementaryQuestion(sourceQuestion, violation);
        if (question == null) {
            return null;
        }
        question.getQuestionData().setDomainEntity(domainService.getDomainEntity(domain.getName()));
        saveQuestion(question.getQuestionData());
        return question;
    }

    public Domain.InterpretSentenceResult judgeSupplementaryQuestion(Question question, List<ResponseEntity> responses, ExerciseAttemptEntity exerciseAttempt) {
        Domain domain = DomainAdapter.getDomain(exerciseAttempt.getExercise().getDomain().getName());
        assertEquals(1, responses.size());
        return domain.judgeSupplementaryQuestion(question, responses.get(0).getLeftAnswerObject());
    }

    public Question solveQuestion(Question question, List<Tag> tags) {
        Domain domain = DomainAdapter.getDomain(question.getQuestionData().getDomainEntity().getName());
        List<BackendFactEntity> solution = backend.solve(
                new ArrayList<>(domain.getQuestionPositiveLaws(question.getQuestionDomainType(), tags)),
                question.getStatementFacts(),
                domain.getSolutionVerbs(question.getQuestionDomainType(), question.getStatementFacts()));
        question.getQuestionData().setSolutionFacts(solution);
        saveQuestion(question.getQuestionData());
        return question;
    }

    public List<ResponseEntity> responseQuestion(Question question, List<Integer> responses) {
        val result = new ArrayList<ResponseEntity>();
        for (val answerId : responses) {
            result.add(makeResponse(question.getAnswerObject(answerId)));
        }
        return result;
    }

    public List<ResponseEntity> responseQuestion(Question question, Long[][] responses) {
        val result = new ArrayList<ResponseEntity>();
        for (Long[] pair: responses) {
            AnswerObjectEntity left = question.getAnswerObject(pair[0].intValue());
            AnswerObjectEntity right = question.getAnswerObject(pair[1].intValue());
            ResponseEntity response = makeResponse(left, right);
            result.add(response);
        }
        return result;
    }

    public Domain.InterpretSentenceResult judgeQuestion(Question question, List<ResponseEntity> responses, List<Tag> tags) {
        Domain domain = DomainAdapter.getDomain(question.getQuestionData().getDomainEntity().getName());
        List<BackendFactEntity> responseFacts = question.responseToFacts(responses);
        List<BackendFactEntity> violations = backend.judge(
                new ArrayList<>(domain.getQuestionNegativeLaws(question.getQuestionDomainType(), tags)),
                question.getStatementFacts(),
                question.getSolutionFacts(),
                responseFacts,
                domain.getViolationVerbs(question.getQuestionDomainType(), question.getStatementFacts())
        );
        return domain.interpretSentence(violations);
    }

    public List<HyperText> explainViolations(Question question, List<ViolationEntity> violations) {
        Domain domain = DomainAdapter.getDomain(question.getQuestionData().getDomainEntity().getName());
        return domain.makeExplanation(violations, FeedbackType.EXPLANATION);
    }

    public Question getQuestion(Long questionId) {
        Question question = generateBusinessLogicQuestion(questionRepository.findById(questionId).get());
        return question;
    }

    public Question getSolvedQuestion(Long questionId) {
        val question = getQuestion(questionId);
        return solveQuestion(question, question.getQuestionData().getExerciseAttempt().getExercise().getTags());
    }

    public QuestionEntity getQuestionEntiity(Long questionId) {
        return questionRepository.findById(questionId).get();
    }

    public void saveQuestion(QuestionEntity question) {
        if (question.getAnswerObjects() != null) {
            for (AnswerObjectEntity answerObject : question.getAnswerObjects()) {
                if (answerObject.getId() == null) {
                    answerObject.setQuestion(question);
                }
            }
            answerObjectRepository.saveAll(question.getAnswerObjects().stream().filter(a -> a.getId() == null)::iterator);
        }

        List<BackendFactEntity> newBackendFacts = new ArrayList<>();
        if (question.getStatementFacts() != null) {
            for (BackendFactEntity fact : question.getStatementFacts()) {
                if (fact.getQuestion() == null) {
                    fact.setQuestion(question);
                    newBackendFacts.add(fact);
                }
            }
        }
        if (question.getSolutionFacts() != null) {
            for (BackendFactEntity fact : question.getSolutionFacts()) {
                if (fact.getQuestion() == null) {
                    fact.setQuestion(question);
                    newBackendFacts.add(fact);
                }
            }
        }
        backendFactRepository.saveAll(newBackendFacts);

        if (question.getInteractions() != null) {
            for (val interactionEntity : question.getInteractions()) {
                if (interactionEntity.getQuestion() == null) {
                    interactionEntity.setQuestion(question);
                }
            }
        }
        questionRepository.save(question);
    }

    public Domain.CorrectAnswer getNextCorrectAnswer(Question question) {
        Domain domain = DomainAdapter.getDomain(question.getQuestionData().getDomainEntity().getName());
        return domain.getAnyNextCorrectAnswer(question);
    }
    
    public Question generateBusinessLogicQuestion(
            ExerciseAttemptEntity exerciseAttempt) {
        
        //Генерируем вопрос
        QuestionRequest qr = strategy.generateQuestionRequest(exerciseAttempt);
        Language userLanguage = exerciseAttempt.getUser().getPreferred_language();
        Domain domain = core.getDomain(
                exerciseAttempt.getExercise().getDomain().getName());
        Question newQuestion =
                domain.makeQuestion(qr, exerciseAttempt.getExercise().getTags(), userLanguage);
        
        saveQuestion(newQuestion.getQuestionData());
        
        return newQuestion;
    }

    public Question generateBusinessLogicQuestion(
            QuestionEntity question) {

        if (question.getQuestionType() == QuestionType.MATCHING) {
            return new Matching(question);
        } else if (question.getQuestionType() == QuestionType.ORDER) {
            return new Ordering(question);
        } else if (question.getQuestionType() == QuestionType.MULTI_CHOICE) {
            return new MultiChoice(question);
        } else {
            return new SingleChoice(question);
        }
    }

    private ResponseEntity makeResponse(AnswerObjectEntity answer) {
        ResponseEntity response = new ResponseEntity();
        response.setLeftAnswerObject(answer);
        response.setRightAnswerObject(answer);
        responseRepository.save(response);
        return response;
    }

    private ResponseEntity makeResponse(AnswerObjectEntity answerL, AnswerObjectEntity answerR) {
        ResponseEntity response = new ResponseEntity();
        response.setLeftAnswerObject(answerL);
        response.setRightAnswerObject(answerR);
        responseRepository.save(response);
        return response;
    }
}

