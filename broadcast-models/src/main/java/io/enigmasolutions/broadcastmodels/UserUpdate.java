package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdate {
    private List<UserUpdateType> updateTypes;
    private TwitterUser old;
    private TwitterUser updated;
}
