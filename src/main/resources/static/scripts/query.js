 url = "https://localhost:20452/";

  function formRequest(methodType,api)
  {
        let request = new XMLHttpRequest () ;
        request.open(methodType,api,false);
        request.setRequestHeader("Content-type", "application/json");
        request.setRequestHeader("Access-Control-Allow-Origin", url);
        console.log(api);
        return request ;
  }

function submitRunQry()
{

}