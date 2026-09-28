import { createContext, useContext, useState } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({children}) {
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('user') || 'null'));

  async function login(username, password) {
    const {data} = await api.post('/auth/login', {username, password});
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
    setUser(data);
    return data;
  }

  function logout() {
    localStorage.clear();
    setUser(null);
    window.location.href = '/';
  }

  return <AuthContext.Provider value={{user, login, logout}}>{children}</AuthContext.Provider>
}
export const useAuth = () => useContext(AuthContext);
