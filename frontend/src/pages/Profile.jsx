import {useEffect,useState} from 'react';
import {useAuth} from '../context/AuthContext';
import api from '../services/api';
import {PageHeader,Loading} from '../components/UI';

export default function Profile(){
 const {user}=useAuth();const [p,setP]=useState(null);
 useEffect(()=>{if(user?.role==='STUDENT')api.get('/me/student').then(r=>setP(r.data));else setP({name:user.username,email:user.username,department:'Administration',course:user.role,studentId:'-'})},[user]);
 if(!p)return <Loading/>;
 return <><PageHeader title="My Profile" subtitle="Account and profile information"/><div className="profile-card"><div className="profile-avatar">{(p.name||p.email)[0].toUpperCase()}</div><div className="profile-grid"><div><span>Name</span><b>{p.name||'-'}</b></div><div><span>Username / Email</span><b>{p.email||user.username}</b></div><div><span>Role</span><b>{user.role}</b></div><div><span>Student ID</span><b>{p.studentId}</b></div><div><span>Department</span><b>{p.department||'-'}</b></div><div><span>Course</span><b>{p.course||'-'}</b></div></div></div></>
}
