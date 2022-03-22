import { writable } from 'svelte/store';

export const username = writable(JSON.parse(window.localStorage.getItem("user") || "{}")?.username || "");
export const role = writable(JSON.parse(window.localStorage.getItem("user") || "{}")?.role || "");
export const jwtToken = writable(JSON.parse(window.localStorage.getItem("user") || "{}")?.token || "");