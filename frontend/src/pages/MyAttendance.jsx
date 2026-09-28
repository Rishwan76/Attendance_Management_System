import {useEffect,useState} from 'react';
import api from '../services/api';
import {PageHeader,Empty,Loading} from '../components/UI';

export default function MyAttendance(){
 const [rows,setRows]=useState([]),[month,setMonth]=useState('all'),[loading,setLoading]=useState(true);
 async function load(){setLoading(true);let params={};if(month!=='all'){const [y,m]=month.split('-').map(Number);params={from:`${month}-01`,to:new Date(y,m,0).toISOString().slice(0,10)}}setRows((await api.get('/me/attendance',{params})).data);setLoading(false)}
 useEffect(()=>{load()},[month]);
 return <><PageHeader title="My Attendance" subtitle="Your personal attendance history"><input className="date-control" type="month" value={month==='all'?'':month} onChange={e=>setMonth(e.target.value||'all')}/></PageHeader>
 <div className="panel"><div className="table-wrap">{loading?<Loading/>:<table><thead><tr><th>Date</th><th>Status</th><th>Marked By</th><th>Last Updated</th></tr></thead><tbody>{rows.map(r=><tr key={r.id}><td>{r.attendanceDate}</td><td><span className={r.status==='PRESENT'?'badge success':'badge danger'}>{r.status}</span></td><td>{r.markedBy}</td><td>{new Date(r.updatedAt).toLocaleString()}</td></tr>)}</tbody></table>}{!loading&&rows.length===0&&<Empty/>}</div></div></>
}
