INSERT INTO `domain` (`version`, `name`) VALUES (1, 'ProgrammingLanguageExpressionDomain');
INSERT INTO `domain` (`version`, `name`) VALUES (1, 'ControlFlowStatementsDomain');

INSERT INTO `course` (`id`, `description`, `name`) VALUES (1, 'тест', 'тест');

INSERT INTO `backend` (`id`, `name`) VALUES (1, 'test');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (5, 1, 1, 0, 1, '10', 'test', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (6, 1, 1, 0, 1, '10', 'test1', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (7, 1, 1, 0, 1, '10', 'test2', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (8, 1, 1, 0, 1, '10', 'test3', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,Python');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (9, 1, 1, 0, 1, '10', 'test_type', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'type');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (10, 1, 1, 0, 1, '10', 'test_all', 8, 0, 1, 1, 'ControlFlowStatementsDomain', 'sequence,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (11, 1, 1, 0, 1, '10', 'test_if', 8, 0, 1, 1, 'ControlFlowStatementsDomain', 'alternative,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (12, 1, 1, 0, 1, '10', 'test_if', 8, 0, 1, 1, 'ControlFlowStatementsDomain', 'while_loop,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (13, 1, 1, 0, 1, '10', 'test_if', 8, 0, 1, 1, 'ControlFlowStatementsDomain', 'do_while_loop,C++');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (14, 1, 1, 0, 1, '10', 'test_operands1', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++,operand_type');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (15, 1, 1, 0, 1, '10', 'test_operands2', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++,operand_type');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (16, 1, 1, 0, 1, '10', 'test_precedence', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,C++,precedence_type');

INSERT INTO `exercise` (`id`, `complexity`, `exercise_type`, `hidden`, `language_id`, `max_retries`, `name`, `time_limit`, `use_guiding_questions`, `backend_id`, `course_id`, `domain_id`, `tags` )
VALUES (17, 1, 1, 0, 1, '10', 'test_explanation', 8, 1, 1, 1, 'ProgrammingLanguageExpressionDomain', 'basics,operators,order,evaluation,C++');

INSERT INTO `user` (`id`, `email`, `first_name`, `last_name`, `login`, `password`, `preferred_language`)
VALUES (1, 'test', 'test', 'test', 'test', 'test', 0);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (4,1,5,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 0, 5);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 1, 5);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 5);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (5,1,8,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 0, 8);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 8);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 1, 8);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (7,1,6,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 0, 6);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 0, 6);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('operator_evaluating_left_operand_first', 1, 6);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 6);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (9,1,7,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 1, 7);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 0, 7);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 7);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (11,1,9,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('type', 0, 9);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 9);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (12,1,5,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 2, 5);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 2, 5);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (13,1,8,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 2, 8);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 2, 8);

INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('trace', 0, 10);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (14,1,14,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 14);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('operands_type', 0, 14);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('binary', 0, 14);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (15,1,15,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 15);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('operands_type', 0, 15);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('ternary', 0, 15);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (16,1,16,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 16);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence_type', 0, 16);

INSERT INTO `exercise_attempt` (`id`, `attempt_status`, `exercise_id`, `user_id` ) VALUES (17,1,17,1);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('precedence', 0, 17);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('associativity', 0, 17);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('operator_evaluating_left_operand_first', 0, 17);
INSERT INTO `exercise_concepts` (`concept_name`, `role_in_exercise`, `exercise_id`) VALUES ('SystemIntegrationTest', 0, 17);
