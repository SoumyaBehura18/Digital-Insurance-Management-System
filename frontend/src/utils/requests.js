import axios from "axios"
 
// Direct backend URL - requires CORS configuration on backend
const base_url = "http://localhost:9090"
 
export async function makeRequestWithoutToken(type, endpoint, body) {
    const url = base_url + endpoint;
    let response = null;

    const config = {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };
 
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
 
export async function makeRequestWithToken(type, endpoint, body) {
    const url = base_url + endpoint;
    const token = localStorage.getItem("token");
    let response = null;

    console.log(`🌐 API Request: ${type} ${url}`);
    console.log(`🔑 Token:`, token ? `${token.substring(0, 20)}...` : 'No token found');

    const config = {
        headers: { 
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    };

    console.log('🔧 Request config:', config);
    if (body) console.log('📦 Request body:', body);

    try {
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

        console.log('✅ API Response received:', response.status, response.statusText);
        console.log('📊 Response data:', response.data);
     
        return response;
        
    } catch (error) {
        console.error('❌ Request failed:', error.message);
        console.error('❌ Request URL:', url);
        console.error('❌ Request headers:', config.headers);
        
        if (error.code === 'ERR_NETWORK') {
            console.error('❌ NETWORK ERROR: Cannot connect to backend server');
            console.error('❌ Make sure backend is running on http://localhost:9090');
        }
        
        if (error.response) {
            console.error('❌ Error response status:', error.response.status);
            console.error('❌ Error response data:', error.response.data);
        }
        
        throw error;
    }
}
 