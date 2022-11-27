const { spawn } = require('node:child_process');
const java = spawn('java', ['-jar', './MorphogenesisSimulationV2.jar']);

const canvas = document.querySelector("#main")
let width = canvas.width
let height = canvas.height
const ctx = canvas.getContext("2d")

const buffer = document.querySelector("#buffer")
const bufferCtx = buffer.getContext("2d")


java.stdout.on('data', (data) => {
    data=data.toString()

    let isFirstPoint = true
    data.split(" ").forEach((command)=>{
        switch (command) {
            case "meshStart":
                bufferCtx.fillStyle="rgba(0,0,0,0.5)"
                bufferCtx.beginPath();
                isFirstPoint = true
                break;
            case "meshEnd":
                bufferCtx.fillStyle="rgba(0,0,0,0.5)"
                bufferCtx.closePath()
                bufferCtx.fill()
                break;
            case "clear":
                bufferCtx.fillStyle="rgb(255,255,255)"
                bufferCtx.fillRect(0,0,width,height);
                break;
            case "finished":
                ctx.drawImage(buffer, 0, 0);
            default:
                bufferCtx.fillStyle="rgba(0,0,0,0.5)"
                parsed = command.split("&").map(e=>parseFloat(e))
                point = transformToScreen(parsed[0],parsed[1])
                if(isFirstPoint){
                    bufferCtx.moveTo(point[0],point[1])
                }else{
                    bufferCtx.lineTo(point[0],point[1])
                }
                isFirstPoint = false
        }
    })
});


function transformToScreen(x,y){
    return [x+width/2,y+height/2]
}
