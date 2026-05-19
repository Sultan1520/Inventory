// Relative path: dev server proxies /api -> :8080 (proxy.conf.json);
// in Docker nginx proxies /api -> backend:8080. No CORS needed.
export const API_URL = '/api';
export const TOKEN_KEY = 'uims_token';
export const USER_KEY = 'uims_user';
