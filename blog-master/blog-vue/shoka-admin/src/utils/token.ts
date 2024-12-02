import Cookies from "js-cookie";

const TokenKey: string = "Token";

// token前缀
export let token_prefix = "Bearer ";

export function getToken() {
  return Cookies.get(TokenKey);
}

// 本地运行记得删除domain
export function setToken(token: string) {
  // 项目线上部署可以取消注释
  return Cookies.set(TokenKey, token);
  // return Cookies.set(TokenKey, token);
}

export function removeToken() {
  // 项目线上部署可以取消注释
  return Cookies.remove(TokenKey);
  // return Cookies.remove(TokenKey);
}
