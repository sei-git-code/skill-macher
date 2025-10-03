package com.ses.salessupport.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ses.salessupport.entity.User;
import com.ses.salessupport.repository.UserRepository;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    /**
     * SupabaseユーザーIDからユーザーを取得
     */
    public Optional<User> getUserBySupabaseUserId(String supabaseUserId) {
        return userRepository.findBySupabaseUserId(supabaseUserId);
    }

    /**
     * ユーザー名からユーザーを取得
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * メールアドレスからユーザーを取得
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 新しいユーザーを作成
     */
    public User createUser(String supabaseUserId, String username, String email, User.UserRole role) {
        User user = new User(supabaseUserId, username, email, role);
        return userRepository.save(user);
    }

    /**
     * ユーザーを更新
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * ユーザーを削除
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * ユーザーを無効化
     */
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        user.setIsActive(false);
        return userRepository.save(user);
    }

    /**
     * ユーザーを有効化
     */
    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + userId));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    /**
     * 現在の認証されたユーザーを取得
     */
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();
            return userRepository.findBySupabaseUserId(userId);
        }
        return Optional.empty();
    }

    /**
     * ユーザー名の重複チェック
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * メールアドレスの重複チェック
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    /**
     * SupabaseユーザーIDの重複チェック
     */
    public boolean isSupabaseUserIdAvailable(String supabaseUserId) {
        return !userRepository.existsBySupabaseUserId(supabaseUserId);
    }

    /**
     * ユーザーが管理者かどうかをチェック
     */
    public boolean isAdmin(User user) {
        return user.getRole() == User.UserRole.ADMIN;
    }

    /**
     * ユーザーがマネージャー以上かどうかをチェック
     */
    public boolean isManagerOrAbove(User user) {
        return user.getRole() == User.UserRole.ADMIN || user.getRole() == User.UserRole.MANAGER;
    }

    /**
     * ユーザーが営業以上かどうかをチェック
     */
    public boolean isSalesOrAbove(User user) {
        return user.getRole() == User.UserRole.ADMIN || 
               user.getRole() == User.UserRole.MANAGER || 
               user.getRole() == User.UserRole.SALES;
    }
}
