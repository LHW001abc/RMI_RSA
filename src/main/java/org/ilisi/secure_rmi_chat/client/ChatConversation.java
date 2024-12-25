package org.ilisi.secure_rmi_chat.client;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatConversation {
    // Définition des couleurs et styles
    private static final String SENT_BG_COLOR = "#4CAF50";      // Vert pour les messages envoyés
    private static final String RECEIVED_BG_COLOR = "#2196F3";  // Bleu pour les messages reçus
    private static final String TEXT_COLOR = "#FFFFFF";         // Texte blanc
    private static final String TIME_COLOR = "#E0E0E0";        // Gris clair pour l'heure
    private static final double MESSAGE_OPACITY = 0.9;          // Légère transparence
    private static final String FONT_FAMILY = "Helvetica Neue"; // Police moderne

    private final User user;
    private final List<Message> messages;

    public ChatConversation(User user) {
        this.user = user;
        this.messages = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public Message addMessage(String message, boolean isSent) {
        Message e = new Message(message, isSent, Instant.now());
        messages.add(e);
        return e;
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public record Message(String message, boolean isSent, Instant timestamp) {

        public Label toLabel(double parentWidth) {
            Label label = toLabel();
            label.setMaxWidth(0.80 * parentWidth); // Réduit à 80% pour un meilleur aspect
            return label;
        }

        public Label toLabel() {
            // Création du conteneur principal
            Label label = new Label();

            // Formatage du message avec l'heure
            String time = DateTimeFormatter
                    .ofPattern("HH:mm")
                    .withZone(ZoneId.systemDefault())
                    .format(timestamp);

            label.setText(message + "\n" + time);

            // Style de base pour tous les messages
            String baseStyle = String.format("""
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-padding: 10px 15px;
                -fx-font-family: '%s';
                -fx-font-size: 14px;
                -fx-opacity: %.2f;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);
                """,
                    isSent ? SENT_BG_COLOR : RECEIVED_BG_COLOR,
                    TEXT_COLOR,
                    FONT_FAMILY,
                    MESSAGE_OPACITY
            );

            // Style spécifique selon si le message est envoyé ou reçu
            String alignmentStyle;
            if (isSent) {
                alignmentStyle = """
                    -fx-background-radius: 20px 20px 0px 20px;
                    -fx-alignment: center-right;
                    -fx-graphic-text-gap: 8;
                    """;
            } else {
                alignmentStyle = """
                    -fx-background-radius: 20px 20px 20px 0px;
                    -fx-alignment: center-left;
                    -fx-graphic-text-gap: 8;
                    """;
            }

            // Application du style complet
            label.setStyle(baseStyle + alignmentStyle);

            // Configuration du label
            label.setMinWidth(100);
            label.setWrapText(true);
            label.setAlignment(isSent ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

            // Ajout de marge pour séparer les messages
            String margin = isSent ? "0 5 8 50" : "0 50 8 5";
            label.setPadding(new javafx.geometry.Insets(
                    0, // top
                    isSent ? 5 : 50, // right
                    8, // bottom
                    isSent ? 50 : 5  // left
            ));

            return label;
        }
    }
}