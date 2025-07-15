package com.example.show_score.restcontroller;

import com.example.show_score.config.WebSocketConfig;
import com.example.show_score.model.MatchBean;
import com.example.show_score.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;

@Controller
@RequestMapping("/ws") // Chemin de base pour toutes les méthodes de ce contrôleur
public class WebSocketController {

    //Outils pour poster des messages
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MatchService matchService;


    // Diffuser les mises à jour de score en temps réel
    @MessageMapping("/score-update") // Chemin : /ws/score-update
    public void updateMatchScore(MatchBean match) {
        try {
            // Mettre à jour le score dans la base de données
            MatchBean updatedMatch = matchService.updateScore(
                    match.getId(),
                    match.getTeam1Score(),
                    match.getTeam2Score()
            );

            // Diffuser la mise à jour à tous les clients connectés
            messagingTemplate.convertAndSend(WebSocketConfig.CHANNEL_NAME, updatedMatch);

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du score: " + e.getMessage());
        }
    }

    // Diffuser les changements de statut de match
    @MessageMapping("/status-update") // Chemin : /ws/status-update
    public void updateMatchStatus(MatchBean match) {
        try {
            // Mettre à jour le statut dans la base de données
            MatchBean updatedMatch = matchService.updateMatchStatus(
                    match.getId(),
                    match.getMatchStatus()
            );

            // Diffuser la mise à jour à tous les clients connectés
            messagingTemplate.convertAndSend(WebSocketConfig.CHANNEL_NAME, updatedMatch);

        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
    }

    //A mettre dans le controller
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (WebSocketConfig.CHANNEL_NAME.equals(headerAccessor.getDestination())) {

            // Envoyer tous les matches en cours ou à venir
            List<MatchBean> allMatches = matchService.getAllMatches();

            messagingTemplate.convertAndSend(WebSocketConfig.CHANNEL_NAME, allMatches);
        }
    }
}
