export const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const TOKEN_KEY = "hms_token";

export function setAuthToken(token) {
  if (token) localStorage.setItem(TOKEN_KEY, token);
  else localStorage.removeItem(TOKEN_KEY);
}

export function getAuthToken() {
  return localStorage.getItem(TOKEN_KEY);
}

function buildHeaders(extra = {}) {
  const headers = { "Content-Type": "application/json", ...extra };
  const token = getAuthToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}


async function handle(res) {
  const text = await res.text(); // read once
  if (!res.ok) throw new Error(text || "Request failed");

  try {
    const parsed = text ? JSON.parse(text) : {};
    return parsed;
  } catch {
    // If backend accidentally returns HTML or something unexpected, return empty array
    return [];
  }
}


export async function apiGet(path) {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: buildHeaders(),
  });
  return handle(res);
}

export async function apiPost(path, data) {
  const isFormData = data instanceof FormData;
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "POST",
    headers: isFormData ? buildHeaders({ "Content-Type": undefined }) : buildHeaders(),
    body: isFormData ? data : JSON.stringify(data ?? {}),
  });
  return handle(res);
}

export async function apiPut(path, data) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "PUT",
    headers: buildHeaders(),
    body: JSON.stringify(data ?? {}),
  });
  return handle(res);
}

export async function apiPatch(path, data) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "PATCH",
    headers: buildHeaders(),
    body: JSON.stringify(data ?? {}),
  });
  return handle(res);
}

export async function apiDelete(path) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "DELETE",
    headers: buildHeaders({ "Content-Type": undefined }),
  });
  return handle(res);
}

