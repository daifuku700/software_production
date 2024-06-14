import React, { useState, useEffect, useRef } from 'react';

const AudioClient = () => {
  const [isRecording, setIsRecording] = useState(false);
  const [audioURL, setAudioURL] = useState('');
  const [audioBlob, setAudioBlob] = useState(null);
  const [message, setMessage] = useState('');
  const socketRef = useRef(null);

  useEffect(() => {
    // WebSocketオブジェクトを生成しサーバとの接続を開始
    const socket = new WebSocket('ws://localhost:8080');
    socketRef.current = socket;

    // メッセージ受信時のイベントハンドラを設定
    socket.onmessage = (event) => {
      setMessage(event.data);
    };

    // クリーンアップの中で、WebSocketのクローズ処理を実行
    return () => {
      socket.close();
    };
  }, []);

  const startRecording = () => {
    setIsRecording(true);
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        const mediaRecorder = new MediaRecorder(stream);
        const chunks = [];

        mediaRecorder.ondataavailable = event => {
          chunks.push(event.data);
        };

        mediaRecorder.onstop = () => {
          const blob = new Blob(chunks, { type: 'audio/wav' });
          const url = URL.createObjectURL(blob);
          setAudioBlob(blob);
          setAudioURL(url);
          setIsRecording(false);
        };

        mediaRecorder.start();
        setTimeout(() => {
          mediaRecorder.stop();
        }, 5000); // 5秒間録音
      })
      .catch(error => {
        console.error('Error accessing media devices.', error);
      });
  };

  const playAudio = () => {
    const audio = new Audio(audioURL);
    audio.play();
  };

  const sendFile = () => {
    if (!audioBlob) {
      console.error('No audio recorded');
      return;
    }

    const reader = new FileReader();
    reader.readAsArrayBuffer(audioBlob);
    reader.onload = () => {
      const arrayBuffer = reader.result;
      const byteArray = new Uint8Array(arrayBuffer);

      const socket = socketRef.current;
      if (socket.readyState === WebSocket.OPEN) {
        const data = new Blob([new Uint8Array([1]), byteArray]);
        socket.send(data);
        console.log('File sent successfully');
      }
    };
  };

  const receiveFile = () => {
    const socket = socketRef.current;
    if (socket.readyState === WebSocket.OPEN) {
      socket.send(new Uint8Array([2]));
      socket.onmessage = (event) => {
        const blob = new Blob([event.data], { type: 'audio/wav' });
        const url = URL.createObjectURL(blob);
        setAudioURL(url);
        console.log('File received successfully');
      };
    }
  };

  return (
    <>
      <button onClick={startRecording} disabled={isRecording}>
        {isRecording ? 'Recording...' : 'Start Recording'}
      </button>
      {audioURL && (
        <>
          <button onClick={playAudio}>Play Audio</button>
          <button onClick={sendFile}>Send Audio</button>
        </>
      )}
      <button onClick={receiveFile}>Receive Audio</button>
      <div>Last received message: {message}</div>
    </>
  );
};

export default AudioClient;
