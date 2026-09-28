import {useEffect,useState} from 'react';
import {useNavigate,useParams} from 'react-router-dom';
import api from '../services/api';
import {PageHeader,ErrorBox,Loading} from '../components/UI';

const empty={studentId:'',name:'',email:'',phone:'',department:'',course:'',year:1,status:true,password:''};

export default function StudentForm(){
 const {id}=useParams(), nav=useNavigate(); const [form,setForm]=useState(empty),[error,setError]=useState(''),[loading,setLoading]=useState(!!id);
 useEffect(()=>{if(id)api.get('/students/'+id).then(r=>setForm({...r.data,password:''})).catch(e=>setError(e.response?.data?.message||'Failed')).finally(()=>setLoading(false))},[id]);
 function change(e){const {name,value,type,checked}=e.target;setForm({...form,[name]:type==='checkbox'?checked:name==='year'?Number(value):value})}
 async function save(e){e.preventDefault();setError('');try{if(id)await api.put('/students/'+id,form);else await api.post('/students',form);nav('/app/students')}catch(e){setError(e.response?.data?.message||'Save failed')}}
 if(loading)return <Loading/>;
 return <><PageHeader title={id?'Edit Student':'Add Student'} subtitle="Enter student information"/><div className="panel form-panel">{error&&<ErrorBox error={error}/>}<form className="form-grid" onSubmit={save}>
 {['studentId','name','email','phone','department','course'].map(k=><div className="field" key={k}><label>{k==='studentId'?'Student ID':k[0].toUpperCase()+k.slice(1)}</label><input name={k} value={form[k]} onChange={change} required={['studentId','name','email'].includes(k)}/></div>)}
 <div className="field"><label>Year</label><input type="number" min="1" max="8" name="year" value={form.year} onChange={change}/></div>
 <div className="field"><label>{id?'New Password (optional)':'Password'}</label><input type="password" name="password" value={form.password} onChange={change} required={!id}/></div>
 <label className="check"><input type="checkbox" name="status" checked={form.status} onChange={change}/> Active student</label>
 <div className="form-actions"><button type="button" className="secondary" onClick={()=>nav('/app/students')}>Cancel</button><button className="primary">Save Student</button></div>
 </form></div></>
}
