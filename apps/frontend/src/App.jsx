import { useCallback, useEffect, useRef, useState } from 'react';

const API_URL = `${import.meta.env.VITE_API_BASE_URL ?? ''}/api/hello`;

export default function App() {
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const latestRequest = useRef(0);

  const loadMessage = useCallback(async () => {
    const requestId = ++latestRequest.current;
    setLoading(true);
    setError('');
    try {
      const response = await fetch(API_URL);
      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`);
      }
      const body = await response.json();
      if (requestId === latestRequest.current) {
        setMessage(body.message);
      }
    } catch (cause) {
      if (requestId === latestRequest.current) {
        setError(cause.message);
        setMessage('');
      }
    } finally {
      if (requestId === latestRequest.current) {
        setLoading(false);
      }
    }
  }, []);

  useEffect(() => {
    loadMessage();
  }, [loadMessage]);

  return (
    <main className="app">
      <h1 data-testid="app-title">Hello World App</h1>
      <p data-testid="greeting">{loading ? 'Loading...' : message}</p>
      {error && <p data-testid="error">{error}</p>}
      <button type="button" data-testid="refresh" onClick={loadMessage}>
        Refresh greeting
      </button>
    </main>
  );
}
