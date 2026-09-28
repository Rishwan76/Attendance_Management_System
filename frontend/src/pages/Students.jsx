import {useEffect,useState} from 'react';
import {Plus,Search,Edit3,UserX} from 'lucide-react';
import {useNavigate} from 'react-router-dom';
import api from '../services/api';
import {PageHeader,Loading,Empty,ErrorBox} from '../components/UI';

export default function Students(){
 const [rows,setRows]=useState([]),[q,setQ]=useState(''),[loading,setLoading]=useState(true),[error,setError]=useState('');const nav=useNavigate();
 async function load(){setLoading(true);try{setRows((await api.get('/students',{params:{q}})).data)}catch(e){setError(e.response?.data?.message||'Failed')}finally{setLoading(false)}}
 useEffect(()=>{load()},[]);
 async function deactivate(id){if(confirm('Deactivate this student?')){await api.delete('/students/'+id);load()}}
 return <><PageHeader title="Student Management" subtitle="Manage students and members"><button className="primary" onClick={()=>nav('/app/students/new')}><Plus size={18}/> Add Student</button></PageHeader>
 <div className="toolbar"><div className="search"><Search size={18}/><input placeholder="Search ID, name or email..." value={q} onChange={e=>setQ(e.target.value)} onKeyDown={e=>e.key==='Enter'&&load()}/></div><button className="secondary" onClick={load}>Search</button></div>
 {error&&<ErrorBox error={error}/>} {loading?<Loading/>:<div className="panel"><div className="table-wrap"><table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th><th>Year</th><th>Status</th><th>Actions</th></tr></thead><tbody>{rows.map(s=><tr key={s.id}><td><b>{s.studentId}</b></td><td>{s.name}</td><td>{s.email}</td><td>{s.department||'-'}</td><td>{s.year||'-'}</td><td><span className={s.status?'badge success':'badge danger'}>{s.status?'Active':'Inactive'}</span></td><td className="actions"><button className="icon-btn" onClick={()=>nav('/app/students/'+s.id+'/edit')}><Edit3 size={16}/></button><button className="icon-btn danger-btn" onClick={()=>deactivate(s.id)}><UserX size={16}/></button></td></tr>)}</tbody></table>{rows.length===0&&<Empty/>}</div></div>}
 </>;
}
