import axios from 'axios'
import {ElMessage} from "element-plus";

const defaultErr = ()=> ElMessage.error('发生了一些错误，请联系管理员')
const defaultFailure = (message) => ElMessage.warning(message)


function post(url,data,success,failure = defaultFailure,err = defaultErr) {
    axios.post(url, data, {
        headers: {
            'Content-type': 'application/x-www-form-urlencoded'
        },
        withCredentials:true   // 带上cookie
    }).then(({data})=>{
        if(data.success){
            success(data.message,data.status)
        }else{
            failure(data.message,data.status)
        }
    }).catch(err)
}

function get(url,success,failure = defaultFailure,err = defaultErr) {
    axios.get(url,  {
        withCredentials:true   // 带上cookie
    }).then(({data})=>{
        if(data.success){
            success(data.message,data.status)
        }else{
            failure(data.message,data.status)
        }
    }).catch(err)
}


export {get,post}