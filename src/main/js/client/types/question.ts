import { OrderQuestionOptions, QuestionOptions } from "./question-options";

export type QuestionType = "SINGLE_CHOICE" | "MULTI_CHOICE" | "MATCHING" | "ORDER";

export type Html = string;

export interface QuestionAnswer {
    id: string,
    text: Html,
}

type QuestionBase = {
    id: string,
    type: QuestionType,
    options: QuestionOptions,
    text: Html,
    answers: QuestionAnswer[],
}

type OrderQuestionBase = QuestionBase & {        
    type: "ORDER",        
    options: OrderQuestionOptions,        
}

type SingleChoiceQuestionBase = QuestionBase & {        
    type: "SINGLE_CHOICE",            
}

type MultiChoiceQuestionBase = QuestionBase & {        
    type: "MULTI_CHOICE",            
}

type MathingQuestionBase = QuestionBase & {        
    type: "MATCHING",            
}

export type Question = OrderQuestionBase | SingleChoiceQuestionBase | MultiChoiceQuestionBase | MathingQuestionBase