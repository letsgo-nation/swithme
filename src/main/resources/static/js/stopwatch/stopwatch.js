import { makeTime, makeSec, switchDisplay, refreshText, recordText } from "./modules/modules.js"
const btn_start = document.querySelector("#stopwatch-start")
const btn_pause = document.querySelector("#stopwatch-pause")
const btn_record = document.querySelector("#stopwatch-record")
const btn_restart = document.querySelector("#stopwatch-restart")
const div_record = document.querySelector("#stopwatch-records")
let clock, clockInterval

btn_start.addEventListener("click", () => {
    clock = document.querySelector("#stopwatch-clock")
    const _ = clock.innerText.split(':')
    let minn = _[0],
        secc = _[1]

    let curSec = makeSec(minn, secc)
    clockInterval = setInterval(() => {
        curSec += 1
        clock.innerText = makeTime(curSec)
    }, 1000)
    btn_start.innerText = "계속"
    btn_restart.hidden = false
    switchDisplay([btn_start, btn_pause, btn_record])
})

btn_pause.addEventListener("click", () => {
    clearInterval(clockInterval)
    switchDisplay([btn_start, btn_pause, btn_record])
})

// btn_record.addEventListener("click", () => {
//     const recTime = document.querySelector("#stopwatch-clock").innerText
//     recordText(recTime, div_record)
// })

btn_record.addEventListener("click", () => {
    const recTime = document.querySelector("#stopwatch-clock").innerText;
    const recordDto = { recordedTime: recTime };

    fetch('/api/record', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(recordDto),
    })
        .then(response => {
            if (response.ok) {
                // 성공적으로 저장되었을 때의 처리
                refreshText(document.querySelector("#stopwatch-clock"), div_record);
                clearInterval(clockInterval);
                btn_start.innerText = "시작";
                btn_start.hidden = false;
                btn_pause.hidden = true;
                btn_record.hidden = true;
                btn_restart.hidden = true;
            } else {
                // 오류 처리
                throw new Error('Network response was not ok');
            }
        })
        .catch(error => {
            // 네트워크 오류 처리
            console.error('Fetch error:', error);
        });
});


btn_restart.addEventListener("click", () => {
    refreshText(document.querySelector("#stopwatch-clock"), div_record)
    clearInterval(clockInterval)
    btn_start.innerText = "시작"
    btn_start.hidden = false
    btn_pause.hidden = true
    btn_record.hidden = true
    btn_restart.hidden = true
})