package com.example.FifteenPuzzle.controllers;
import com.example.FifteenPuzzle.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@AllArgsConstructor
public class GameController {
    private GameService gameService;
    @GetMapping("/game")
    public String showMatrix(Model model) {
        model.addAttribute("matrix", gameService.getMatrix().getMatrix());
        return "game";
    }
    @GetMapping("/refresh_matrix")
    public String refreshMatrix() {
        gameService.refreshMatrix();
        return "redirect:/game";
    }
    @GetMapping("/click_chip")
    public String clickChip(@RequestParam int x, @RequestParam int y) {
        gameService.clickAndCheckChip(x,y);
        if(gameService.checkWin()) {
            return "win";
        }
        return "redirect:/game";
    }

}