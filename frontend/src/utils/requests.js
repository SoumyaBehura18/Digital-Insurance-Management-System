import axios from "axios"
 
const base_url = "http://localhost:8080"
 
export async function makeRequestWithoutToken(type, endpoint, body) {
    const url = base_url + endpoint;
    let response = null;
 
    switch (type) {
        case "GET":
            response = await axios.get(url);
            break;
        case "POST":
            response = await axios.post(url, body);
            break;
        case "PATCH":
            response = await axios.patch(url, body);
            break;
        case "DELETE":
            response = await axios.delete(url);
            break;
        default:
            throw new Error(`Unsupported request type: ${type}`);
    }
 
    return response;
}
 
export async function makeRequestWithToken(type, endpoint, body) {
    const url = base_url + endpoint;
    const token = localStorage.getItem("authToken");
    let response = null;
 
    const config = {
        headers: { Authorization: `Bearer ${token}` }
    };
 
    switch (type) {
        case "GET":
            response = await axios.get(url, config);
            break;
        case "POST":
            response = await axios.post(url, body, config);
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
 