package com.ses.salessupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {

    /**
     * ログインページ
     */
    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    /**
     * ログイン成功後のリダイレクト
     */
    @GetMapping("/login-success")
    public String loginSuccess() {
        return "redirect:/";
    }

    /**
     * ログイン失敗時のリダイレクト
     */
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", "ログインに失敗しました。ユーザー名とパスワードを確認してください。");
        return "auth/login";
    }

    /**
     * ログアウト
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }

    /**
     * アクセス拒否ページ
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error", "アクセスが拒否されました。適切な権限が必要です。");
        return "error/403";
    }
}
