document.getElementById('sendButton').addEventListener('click', function() {
    const userRole = "user"; // 예시로 'user'를 설정, 실제 구현에서는 사용자 입력에 따라 결정
    const userInput = document.getElementById('userInput').value;
    if (userInput.trim() !== '') {
        sendMessage(userRole, userInput);
    }
});

async function sendMessage(role, content) {
    const responseElement = document.getElementById('chatMessages');
    try {
        const response = await fetch('/openai-chat/chat/question', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({role: role, content: content }),
        });
        const data = await response.json();

        responseElement.innerHTML += `<div class="message">You: ${content}</div>`;
        responseElement.innerHTML += `<div class="message">GPT: ${data.message.content}</div>`;

        document.getElementById('userInput').value = ''; // Clear input field after sending

    } catch (error) {
        console.error('Error:', error);
        responseElement.innerHTML += `<div class="message">Error sending message.</div>`;
    }
}
