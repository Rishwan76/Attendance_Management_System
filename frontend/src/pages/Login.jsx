import {useState} from 'react';
import {useAuth} from '../context/AuthContext';
import {useNavigate} from 'react-router-dom';
import {LockKeyhole, User, LogIn} from 'lucide-react';
import '../styles.css';

export default function Login(){
  const {login}=useAuth(), nav=useNavigate();
  const [username,setUsername]=useState('admin'), [password,setPassword]=useState('Admin@123');
  const [error,setError]=useState(''), [loading,setLoading]=useState(false);
  async function submit(e){e.preventDefault();setError('');setLoading(true);try{const u=await login(username,password);nav('/app')}catch(err){setError(err.response?.data?.message||'Login failed')}finally{setLoading(false)}}
  return <div className="login-page">
    <div className="login-card">
      <div className="login-brand"><div className="logo big">A</div><h1>AttendPro</h1><p>Attendance Management System</p></div>
      <form onSubmit={submit}>
        {error&&<div className="error-box">{error}</div>}
        <label>Username / Email</label><div className="input-icon"><User size={18}/><input value={username} onChange={e=>setUsername(e.target.value)} required/></div>
        <label>Password</label><div className="input-icon"><LockKeyhole size={18}/><input type="password" value={password} onChange={e=>setPassword(e.target.value)} required/></div>
        <button className="primary full" disabled={loading}><LogIn size={18}/>{loading?'Signing in...':'Sign in'}</button>
      </form>
      <div className="demo-box"><b>Demo accounts</b><span>Admin: admin / Admin@123</span><span>Staff: staff / Staff@123</span><span>Student: student1@example.com / Student@123</span></div>
    </div>
  </div>
}
