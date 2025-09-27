import axios from "axios";

const base_url = "http://localhost:9090";

export async function makeRequestWithoutToken(type, endpoint, body = null) {
  const url = base_url + endpoint;
  let response = null;

  switch (type.toUpperCase()) {
    case "GET":
      response = await axios.get(url);
      break;
    case "POST":
      response = await axios.post(url, body, {
        headers: { "Content-Type": "application/json" },
      });
      break;
    case "PATCH":
      response = await axios.patch(url, body, {
        headers: { "Content-Type": "application/json" },
      });
      break;
    case "DELETE":
      response = await axios.delete(url);
      break;
    default:
      throw new Error(`Unsupported request type: ${type}`);
  }

  return response;
}

export async function makeRequestWithToken(type, endpoint, body = null) {
  const url = base_url + endpoint;

  // Get the token from multiple fallbacks in localStorage
  const storedUser = localStorage.getItem("currentUser");
  const parsedUser = storedUser ? JSON.parse(storedUser) : null;
  const token =
    parsedUser?.token ||
    localStorage.getItem("authToken") ||
    localStorage.getItem("token");

  if (!token) {
    throw new Error("No auth token found in localStorage");
  }

  const config = {
    // headers: {
    //     'Content-Type': 'application/json',
    //     'Accept': 'application/json',
    //     'Authorization': `Bearer ${token}`
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  };

  let response = null;
  switch (type) {
    case "GET":
      response = await axios.get(url, config);
      break;
    case "POST":
      response = await axios.post(url, body, config);
      break;
    case "PUT":
      response = await axios.put(url, body, config);
      break;
    case "PATCH":
      response = await axios.patch(url, body, config);
      break;
    case "DELETE":
      response = await axios.delete(url, config);
      break;
    default:
      throw new Error(`Unsupported request type: ${type}`);
  }

  return response;
}

// Attach a simple response interceptor-like wrapper to auto-handle 401/403
// We don't register a global axios interceptor to keep scope local to this util.
// Instead, wrap each call site via this helper to detect auth errors.
export async function requestWithAuth(type, endpoint, body) {
  try {
    return await makeRequestWithToken(type, endpoint, body);
  } catch (err) {
    const status = err?.response?.status;
    if (status === 401 || status === 403) {
      // Clear stale credentials and redirect to login
      try {
        localStorage.removeItem("currentUser");
        localStorage.removeItem("authToken");
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
      } catch (_) {
        // intentionally ignore storage errors (ESLint no-empty fix)
        // noop
      }
      // Best-effort redirect without importing router here
      if (typeof window !== "undefined") {
        const current = window.location.pathname + window.location.search;
        const loginUrl =
          "/login" +
          (current && current !== "/login"
            ? `?redirect=${encodeURIComponent(current)}`
            : "");
        window.location.assign(loginUrl);
      }
    }
    throw err;
  }
}
