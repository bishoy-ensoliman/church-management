import axios from 'axios';

export const jwtTokenGlobal = {
    token: ""
};

const API_ROOT = process.env.isProd ? "backendProdBaseUrl" : "backendDevBaseUrl";

// const axiosRequestInterceptor = (config) => {
//     config.headers.Authorization = "Bearer " + jwtTokenGlobal.token;
// };
// axios.interceptors.request.use(axiosRequestInterceptor);

/** Replace this with your own entities **/
const requests = {
    getAuthenticated: (url, token) => {
        return axios.get(`${API_ROOT}${url}`,{
            headers: {
                Authorization: "Bearer " + token
            }
        })
    },
    get: (url) => {
        return axios.get(`${API_ROOT}${url}`)
    },
    download: (url, cancelToken) => {
        return axios.get(`${API_ROOT}${url}`, { cancelToken: cancelToken, responseType: 'blob' })
    },
    post: (url, body, config) => axios.post(`${API_ROOT}${url}`, body, config),
    put: (url, body) => axios.put(`${API_ROOT}${url}`, body),
    putAuthenticated: (url, body, token) => axios.put(`${API_ROOT}${url}`, body, {
        headers: {
            Authorization: "Bearer " + token
        }
    }),
    delete: (url, token) => axios.delete(`${API_ROOT}${url}`, {
        headers: {
            Authorization: "Bearer " + token
        }
    }),
    deleteUnAuth: (url) => axios.delete(`${API_ROOT}${url}`),
};

const cancelTokenSource = function () {
    return axios.CancelToken.source();
};

const cancel = function (CancelTokenSource) {
    CancelTokenSource.cancel('Operation canceled due to new request.')
};

const User = {
    authenticate: (username, password) => requests.post(`/apis/v1/user/authenticate`, { username, password })
};

const Liturgie = {
    get: () => requests.get(`/apis/v1/liturgie`),
    putAuthenticated: (liturgie, token) => requests.putAuthenticated(`/apis/v1/liturgie`,liturgie, token),
    // post: (liturgie, token) => requests.post(`/apis/v1/liturgie`,liturgie, token),
    delete: (liturgieUUID, forceDelete, token) => requests.delete(`/apis/v1/liturgie/${liturgieUUID}?forceDelete=${forceDelete}`, token)
};

const Reservation = {
    put: (data) => requests.put(`/apis/v1/reservation`, data),
    delete: (uuid, password) => requests.deleteUnAuth(`/apis/v1/reservation/${uuid}?password=${password}`),
    getAuthenticated: (token) => requests.getAuthenticated(`/apis/v1/reservation`, token),
    get: (firstName, lastName) => requests.get(`/apis/v1/reservation?firstName=${firstName}&lastName=${lastName}`)
}

const UserReservation = {
    get: (username) => requests.get(`/apis/v1/user-reservation?username=${username}`)
}

const Post = {
    get: (pageIndex, numberOfPosts) => requests.get(`/apis/v1/post?pageIndex=${pageIndex}&numberOfPosts=${numberOfPosts}`)
}

export default {
    cancelTokenSource,
    cancel,
    User,
    Liturgie,
    Reservation,
    UserReservation,
    Post
};
