export const BASE_URL = "https://bjt6d7s9-8080.inc1.devtunnels.ms";

export async function apiGet(path) {
  const res = await fetch(`${BASE_URL}${path}`);
  if (!res.ok) throw new Error((await res.text()) || "Request failed");
  return res.json();
}

export async function apiPost(path, data) {
  const res = await fetch(`${BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error((await res.text()) || "Request failed");
  return res.json().catch(() => ({}));
}
