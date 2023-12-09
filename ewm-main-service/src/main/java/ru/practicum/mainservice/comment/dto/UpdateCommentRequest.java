package ru.practicum.mainservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequest {
    private String text;
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH,
        CHECK,
        CANCEL
    }
}
