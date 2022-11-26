const { spawn } = require('node:child_process');
const java = spawn('java', ['-jar', './MorphogenesisSimulationV2.jar']);

const canvas = document.querySelector("canvas")
const ctx = canvas.getContext("2d")
ctx.fillStyle="rgb(0,0,0)"

java.stdout.on('data', (data) => {
    parsed = data.toString().split(" ").flatMap(e=>parseInt(e))
    ctx.fillRect(parsed[0],parsed[1],2,2)
});
