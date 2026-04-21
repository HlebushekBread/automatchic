package net.softloaf.automatchic.common.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationEvent {
    private String email;
    private String subject;
    private String link;
}
