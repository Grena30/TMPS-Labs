package messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MessageStorageImpl implements MessageStorage{

        private Map<String, List<Message>> userMessages = new HashMap<>();

        @Override
        public void saveMessage(Message message) {
                if (message != null) {
                        String userId = message.getReceiverId();
                        List<Message> messages = userMessages.computeIfAbsent(userId, k -> new ArrayList<>());
                        messages.add(message);
                }
        }

        @Override
        public List<Message> getMessagesByUser(String userId) {
                return userMessages.getOrDefault(userId, new ArrayList<>());
        }
}
