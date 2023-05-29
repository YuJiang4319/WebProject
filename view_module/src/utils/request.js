import axios from "axios"

const request = axios.create({
    baseURL:"http://121.199.44.151:4000",
    timeout:10000,
})

export default request