
import { observer } from 'mobx-react';
import * as React from 'react';
import { Button } from 'react-bootstrap';
import { exerciseStore } from "../../stores/exercise-store";

export const GenerateNextAnswerBtn = observer(() => {
    const onClicked = () => {
        exerciseStore.generateNextCorrectAnswer();
    };

    const { sessionInfo, currentQuestion, currentAttempt } = exerciseStore;
    if (!currentQuestion || !sessionInfo || !currentAttempt) {
        return null;
    }

    return (
        <div style={{ marginTop: '20px'}}>            
            <Button onClick={onClicked} variant="primary">Next correct answer</Button>
        </div>
    )
})
