import {useEffect,useState} from 'react';
import {Save,CalendarDays} from 'lucide-react';
import api from '../services/api';
import {PageHeader,Loading,ErrorBox} from '../components/UI';

export default function DailyAttendance(){
 const [date,setDate]=useState(new Date().toISOString().slice(0,10)),[students,setStudents]=useState([]),[records,setRecords]=useState({}),[error,setError]=useState(''),[saving,setSaving]=useState(false);
 async function load(){setError('');try{const [s,a]=await Promise.all([api.get('/students'),api.get('/attendance/date/'+date)]);setStudents(s.data.filter(x=>x.status));const map={};a.data.forEach(x=>map[x.studentId]=x);setRecords(map)}catch(e){setError(e.response?.data?.message||'Failed')}}
 useEffect(()=>{load()},[date]);
 function setStatus(studentDbId,status){setRecords({...records,[studentDbId]:{...records[studentDbId],studentId:studentDbId,status,attendanceDate:date}})}
 async function save(){setSaving(true);setError('');try{for(const s of students){const r=records[s.id];if(!r?.status)continue;if(r.id)await api.put('/attendance/'+r.id,{studentId:s.id,attendanceDate:date,status:r.status});else await api.post('/attendance',{studentId:s.id,attendanceDate:date,status:r.status})}await load()}catch(e){setError(e.response?.data?.message||'Could not save attendance')}finally{setSaving(false)}}
 return <><PageHeader title="Daily Attendance" subtitle="Mark or update attendance"><div className="date-input"><CalendarDays size={18}/><input type="date" value={date} max={new Date().toISOString().slice(0,10)} onChange={e=>setDate(e.target.value)}/></div><button className="primary" onClick={save} disabled={saving}><Save size={18}/>{saving?'Saving...':'Save Attendance'}</button></PageHeader>
 {error&&<ErrorBox error={error}/>}<div className="panel"><div className="table-wrap"><table><thead><tr><th>Student ID</th><th>Student Name</th><th>Department</th><th>Status</th></tr></thead><tbody>{students.map(s=><tr key={s.id}><td>{s.studentId}</td><td><b>{s.name}</b></td><td>{s.department||'-'}</td><td><div className="segmented"><button className={records[s.id]?.status==='PRESENT'?'active present':''} onClick={()=>setStatus(s.id,'PRESENT')}>Present</button><button className={records[s.id]?.status==='ABSENT'?'active absent':''} onClick={()=>setStatus(s.id,'ABSENT')}>Absent</button></div></td></tr>)}</tbody></table></div></div></>
}
