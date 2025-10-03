// SES営業支援ツール - メインJavaScript

// DOM読み込み完了後に実行
document.addEventListener("DOMContentLoaded", function () {
  // アラートの自動非表示
  initializeAlerts();

  // フォームのバリデーション
  initializeFormValidation();

  // テーブルのソート機能
  initializeTableSorting();

  // モーダルの初期化
  initializeModals();

  // ツールチップの初期化
  initializeTooltips();

  // チャートの初期化
  initializeCharts();
});

// アラートの自動非表示
function initializeAlerts() {
  const alerts = document.querySelectorAll(".alert");
  alerts.forEach((alert) => {
    // 5秒後に自動で非表示
    setTimeout(() => {
      if (alert.classList.contains("show")) {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
      }
    }, 5000);
  });
}

// フォームのバリデーション
function initializeFormValidation() {
  const forms = document.querySelectorAll(".needs-validation");
  forms.forEach((form) => {
    form.addEventListener("submit", function (event) {
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add("was-validated");
    });
  });
}

// テーブルのソート機能
function initializeTableSorting() {
  const sortableHeaders = document.querySelectorAll(".sortable");
  sortableHeaders.forEach((header) => {
    header.addEventListener("click", function () {
      const table = this.closest("table");
      const tbody = table.querySelector("tbody");
      const rows = Array.from(tbody.querySelectorAll("tr"));
      const columnIndex = Array.from(this.parentNode.children).indexOf(this);
      const isAscending = this.classList.contains("sort-asc");

      // ソート方向を切り替え
      sortableHeaders.forEach((h) =>
        h.classList.remove("sort-asc", "sort-desc")
      );
      this.classList.add(isAscending ? "sort-desc" : "sort-asc");

      // 行をソート
      rows.sort((a, b) => {
        const aText = a.children[columnIndex].textContent.trim();
        const bText = b.children[columnIndex].textContent.trim();

        if (isAscending) {
          return bText.localeCompare(aText);
        } else {
          return aText.localeCompare(bText);
        }
      });

      // ソートされた行を再配置
      rows.forEach((row) => tbody.appendChild(row));
    });
  });
}

// モーダルの初期化
function initializeModals() {
  const modals = document.querySelectorAll(".modal");
  modals.forEach((modal) => {
    modal.addEventListener("show.bs.modal", function (event) {
      // モーダル表示時の処理
      const modalBody = this.querySelector(".modal-body");
      if (modalBody) {
        modalBody.scrollTop = 0;
      }
    });

    modal.addEventListener("hidden.bs.modal", function (event) {
      // モーダル非表示時の処理
      const form = this.querySelector("form");
      if (form) {
        form.reset();
        form.classList.remove("was-validated");
      }
    });
  });
}

// ツールチップの初期化
function initializeTooltips() {
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
  });
}

// チャートの初期化
function initializeCharts() {
  // Chart.jsのデフォルト設定
  Chart.defaults.font.family =
    "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif";
  Chart.defaults.font.size = 12;
  Chart.defaults.color = "#5a5c69";

  // レスポンシブ対応
  Chart.defaults.responsive = true;
  Chart.defaults.maintainAspectRatio = false;
}

// ユーティリティ関数

// 日付フォーマット
function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleDateString("ja-JP", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
}

// 数値フォーマット（カンマ区切り）
function formatNumber(number) {
  return new Intl.NumberFormat("ja-JP").format(number);
}

// 通貨フォーマット
function formatCurrency(amount) {
  return new Intl.NumberFormat("ja-JP", {
    style: "currency",
    currency: "JPY",
  }).format(amount);
}

// パーセンテージフォーマット
function formatPercentage(value) {
  return new Intl.NumberFormat("ja-JP", {
    style: "percent",
    minimumFractionDigits: 1,
    maximumFractionDigits: 1,
  }).format(value / 100);
}

// ローディング表示
function showLoading(element) {
  const loadingHtml = `
        <div class="d-flex justify-content-center align-items-center">
            <div class="spinner-border spinner-border-sm me-2" role="status">
                <span class="visually-hidden">読み込み中...</span>
            </div>
            読み込み中...
        </div>
    `;
  element.innerHTML = loadingHtml;
}

// エラーメッセージ表示
function showError(message, element) {
  const errorHtml = `
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
  element.innerHTML = errorHtml;
}

// 成功メッセージ表示
function showSuccess(message, element) {
  const successHtml = `
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle me-2"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
  element.innerHTML = successHtml;
}

// AJAXリクエストのヘルパー関数
function makeRequest(url, options = {}) {
  const defaultOptions = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "X-Requested-With": "XMLHttpRequest",
    },
  };

  const mergedOptions = { ...defaultOptions, ...options };

  return fetch(url, mergedOptions)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    })
    .catch((error) => {
      console.error("Request failed:", error);
      throw error;
    });
}

// フォームデータの送信
function submitForm(formElement, options = {}) {
  const formData = new FormData(formElement);
  const url = formElement.action || window.location.href;

  const requestOptions = {
    method: "POST",
    body: formData,
    ...options,
  };

  return makeRequest(url, requestOptions);
}

// 確認ダイアログ
function confirmAction(message, callback) {
  if (confirm(message)) {
    callback();
  }
}

// データテーブルの検索機能
function initializeDataTableSearch() {
  const searchInput = document.getElementById("searchInput");
  const table = document.getElementById("dataTable");

  if (searchInput && table) {
    searchInput.addEventListener("input", function () {
      const searchTerm = this.value.toLowerCase();
      const rows = table.querySelectorAll("tbody tr");

      rows.forEach((row) => {
        const text = row.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
          row.style.display = "";
        } else {
          row.style.display = "none";
        }
      });
    });
  }
}

// ページネーション
function initializePagination() {
  const paginationLinks = document.querySelectorAll(".pagination .page-link");
  paginationLinks.forEach((link) => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      const page = this.dataset.page;
      if (page) {
        loadPage(page);
      }
    });
  });
}

// ページ読み込み
function loadPage(page) {
  const url = new URL(window.location);
  url.searchParams.set("page", page);
  window.location.href = url.toString();
}

// フィルター機能
function initializeFilters() {
  const filterButtons = document.querySelectorAll(".filter-btn");
  filterButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const filter = this.dataset.filter;
      const value = this.dataset.value;

      // アクティブ状態を切り替え
      filterButtons.forEach((btn) => btn.classList.remove("active"));
      this.classList.add("active");

      // フィルターを適用
      applyFilter(filter, value);
    });
  });
}

// フィルター適用
function applyFilter(filter, value) {
  const url = new URL(window.location);
  if (value) {
    url.searchParams.set(filter, value);
  } else {
    url.searchParams.delete(filter);
  }
  window.location.href = url.toString();
}

// エクスポート機能
function exportData(format) {
  const url = new URL(window.location);
  url.searchParams.set("export", format);
  window.open(url.toString(), "_blank");
}

// 印刷機能
function printPage() {
  window.print();
}

// ダークモード切り替え
function toggleDarkMode() {
  const body = document.body;
  const isDark = body.classList.contains("dark-mode");

  if (isDark) {
    body.classList.remove("dark-mode");
    localStorage.setItem("darkMode", "false");
  } else {
    body.classList.add("dark-mode");
    localStorage.setItem("darkMode", "true");
  }
}

// ダークモードの初期化
function initializeDarkMode() {
  const darkMode = localStorage.getItem("darkMode");
  if (darkMode === "true") {
    document.body.classList.add("dark-mode");
  }
}

// 初期化時にダークモードを設定
initializeDarkMode();

// グローバル関数として公開
window.SESApp = {
  formatDate,
  formatNumber,
  formatCurrency,
  formatPercentage,
  showLoading,
  showError,
  showSuccess,
  makeRequest,
  submitForm,
  confirmAction,
  exportData,
  printPage,
  toggleDarkMode,
};
