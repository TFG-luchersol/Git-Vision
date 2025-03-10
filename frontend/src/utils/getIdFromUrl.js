export default function getIdFromUrl(index=-1) {
    const splits = window.location.pathname.split('/')
    if(index < 0)
        index = splits.length + index
    
    return splits[index];
}