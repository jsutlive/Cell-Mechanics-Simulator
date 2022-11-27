const { spawn } = require('node:child_process');
const java = spawn('java', ['-jar', './MorphogenesisSimulationV2.jar']);

const canvas = document.querySelector("canvas")
let width = canvas.width
let height = canvas.height
const ctx = canvas.getContext("2d")

java.stdout.on('data', (data) => {
    data=data.toString()

    let isFirstPoint = true
    data.split(" ").forEach((command)=>{
        switch (command) {
            case "meshStart":
                ctx.fillStyle="rgba(0,0,0,0.5)"
                ctx.beginPath();
                isFirstPoint = true
                break;
            case "meshEnd":
                ctx.fillStyle="rgba(0,0,0,0.5)"
                ctx.closePath()
                ctx.fill()
                break;
            case "clear":
                ctx.fillStyle="rgb(255,255,255)"
                ctx.fillRect(0,0,width,height);
                break;
            default:
                parsed = command.split("&").map(e=>parseFloat(e))
                point = transformToScreen(parsed[0],parsed[1])
                if(isFirstPoint){
                    ctx.moveTo(point[0],point[1])
                }else{
                    ctx.lineTo(point[0],point[1])
                }
                isFirstPoint = false
        }
    })
});

function drawMesh(data){
    parsed = data.toString().split(" ").map(e=>parseFloat(e))
    ctx.beginPath();
    point = transformToScreen(parsed[0],parsed[1])
    ctx.moveTo(point[0],point[1])
    for(let i = 2; i<parsed.length; i+=2){
        point = transformToScreen(parsed[i],parsed[i+1])
        ctx.lineTo(point[0],point[1])
    }
    ctx.closePath()
    ctx.fill()
}


function transformToScreen(x,y){
    return [x+width/2,y+height/2]
}
