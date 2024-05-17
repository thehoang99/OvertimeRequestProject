const messages = [
    ['Whoops.', 'Oops.', 'Excuse me.', 'Oh Dear.', 'Well poo.', 'Hm...', 'This is awkward.', 'Well gosh!'],
    ['It appears', 'Looks like', 'Unfortunately,', 'It just so happens', 'Sadly,', 'Seemingly from nowhere'],
    ['there was an error.', 'we goofed up.', 'a bad thing happend.', 'the server crashed.', 'a bug appeared.', 'someone did a naughty.', 'pixies got into the server!', 'the server threw a tantrum.', 'the website had a bad day.', 'our code pooped out.'],
    ['Sorry.', 'Apologies.', 'Our bad.', 'Sad day.', 'We are quite contrite.', 'Beg pardon.']
];

const messageElements = [
    document.querySelector('#js-whoops'),
    document.querySelector('#js-appears'),
    document.querySelector('#js-error'),
    document.querySelector('#js-apology')
];

const widthElement = document.querySelector("#js-hidden");

let lastMessageType = -1;

function getNewSwapIndex() {
    let newMsgIndex = Math.floor(Math.random() * messages.length);
    while (lastMessageType === newMsgIndex) {
        newMsgIndex = Math.floor(Math.random() * messages.length);
    }
    return newMsgIndex;
}

function checkWidthSet(index) {
    if (messageElements[index].style.width === false) {
        messageElements[index].style.width = `${messageElements[index].clientWidth}px`;
    }
}

function getNewMessage(index) {
    let arrayMsg = messages[index];
    let previousMsg = messageElements[index].innerText;
    let newMsgIndex = Math.floor(Math.random() * arrayMsg.length);
    let newMsg = arrayMsg[newMsgIndex];

    while (newMsgIndex === previousMsg) {
        newMsgIndex = Math.floor(Math.random() * arrayMsg.length);
        newMsg = arrayMsg[newMsgIndex];
    }
    return newMsg;
}

function setupMessage() {
    messageElements.forEach((element, index) => {
        let newMsg = getNewMessage(index);
        element.innerText = newMsg;
    });
}

function calculateWidth(element, message) {
    widthElement.innerText = message;
    let newWidth = widthElement.getBoundingClientRect().width;
    element.style.width = `${newWidth}px`;
}

function swapMessage() {
    let toSwapIndex = getNewSwapIndex();
    let newMsg = getNewMessage(toSwapIndex);

    messageElements[toSwapIndex].style.lineHeight = '0';
    setTimeout(() => {
        checkWidthSet(toSwapIndex, messageElements[toSwapIndex].innerText);
        messageElements[toSwapIndex].innerText = newMsg;
        calculateWidth(messageElements[toSwapIndex], newMsg);
    }, 200);
    setTimeout(() => {
        messageElements[toSwapIndex].style.lineHeight = '1.2';
    }, 400);
}

document.addEventListener('DOMContentLoaded', (event) => {
    setupMessage();
    setInterval(() => {
        swapMessage();
    }, 4000)
});


