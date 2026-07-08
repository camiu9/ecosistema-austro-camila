import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080';

const initialForm = {
  cedula: '1710034065',
  requestedAmount: '100',
  termYears: '2',
  salary: '1500',
};

function App() {
  const [form, setForm] = useState(initialForm);
  const [evaluations, setEvaluations] = useState([]);
  const [latestResult, setLatestResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [listLoading, setListLoading] = useState(false);
  const [error, setError] = useState('');

  const canSubmit = useMemo(() => {
    return form.cedula.length === 10
      && Number(form.requestedAmount) > 0
      && Number(form.termYears) > 0
      && Number(form.salary) > 0
      && !loading;
  }, [form, loading]);

  useEffect(() => {
    loadEvaluations();
  }, []);

  /**
   * Actualiza un campo del formulario manteniendo el resto de valores.
   *
   * @param {string} field nombre del campo a actualizar.
   * @param {string} value valor ingresado por el usuario.
   */
  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  /**
   * Consulta el historial de evaluaciones desde el orquestador.
   */
  async function loadEvaluations() {
    setListLoading(true);
    try {
      const response = await fetch(`${API_URL}/v1/credit-evaluations`);
      if (!response.ok) {
        throw new Error('No se pudo cargar el historial');
      }
      setEvaluations(await response.json());
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setListLoading(false);
    }
  }

  /**
   * Envia la solicitud de evaluacion y refresca el historial cuando se crea correctamente.
   *
   * @param {SubmitEvent} event evento del formulario.
   */
  async function submitEvaluation(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
    setLatestResult(null);

    try {
      const response = await fetch(`${API_URL}/v1/credit-evaluations`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          cedula: form.cedula,
          requestedAmount: Number(form.requestedAmount),
          termYears: Number(form.termYears),
          salary: Number(form.salary),
        }),
      });

      const payload = await response.json();
      if (!response.ok) {
        const details = payload.details?.join(', ');
        throw new Error(details || payload.message || 'La evaluacion fallo');
      }

      setLatestResult(payload);
      await loadEvaluations();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="app-shell">
      <section className="header-band">
        <div>
          <p className="eyebrow">Banco Austro</p>
          <h1>Evaluacion de creditos</h1>
          <p className="subtitle">Orquestacion de riesgo, capacidad de pago y decision final.</p>
        </div>
      </section>

      <section className="workspace">
        <form className="panel form-panel" onSubmit={submitEvaluation}>
          <h2>Nueva solicitud</h2>
          <label>
            Cedula
            <input
              value={form.cedula}
              onChange={(event) => updateField('cedula', event.target.value.replace(/\D/g, '').slice(0, 10))}
              inputMode="numeric"
              minLength="10"
              maxLength="10"
              required
            />
          </label>
          <label>
            Monto solicitado
            <input
              value={form.requestedAmount}
              onChange={(event) => updateField('requestedAmount', event.target.value)}
              type="number"
              min="0.01"
              step="0.01"
              required
            />
          </label>
          <label>
            Tiempo en anios
            <input
              value={form.termYears}
              onChange={(event) => updateField('termYears', event.target.value)}
              type="number"
              min="1"
              step="1"
              required
            />
          </label>
          <label>
            Salario
            <input
              value={form.salary}
              onChange={(event) => updateField('salary', event.target.value)}
              type="number"
              min="0.01"
              step="0.01"
              required
            />
          </label>

          <button type="submit" disabled={!canSubmit}>
            {loading ? 'Evaluando...' : 'Evaluar credito'}
          </button>

          {error && <p className="error-message">{error}</p>}
        </form>

        <section className="panel result-panel" aria-live="polite">
          <h2>Resultado</h2>
          {latestResult ? (
            <div className={`decision ${latestResult.status === 'APPROVED' ? 'approved' : 'rejected'}`}>
              <span>{latestResult.status === 'APPROVED' ? 'Aprobado' : 'Rechazado'}</span>
              <dl>
                <div><dt>Score</dt><dd>{latestResult.score}</dd></div>
                <div><dt>Deuda mensual</dt><dd>{formatMoney(latestResult.monthlyDebt)}</dd></div>
                <div><dt>Capacidad</dt><dd>{formatMoney(latestResult.debtCapacity)}</dd></div>
              </dl>
            </div>
          ) : (
            <p className="empty-state">Completa el formulario para ver la decision final.</p>
          )}
        </section>
      </section>

      <section className="panel history-panel">
        <div className="history-header">
          <h2>Evaluaciones realizadas</h2>
          <button className="secondary-button" type="button" onClick={loadEvaluations} disabled={listLoading}>
            {listLoading ? 'Actualizando...' : 'Actualizar'}
          </button>
        </div>
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Cedula</th>
                <th>Monto</th>
                <th>Score</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {evaluations.map((evaluation) => (
                <tr key={evaluation.id}>
                  <td>{formatDate(evaluation.createdAt)}</td>
                  <td>{evaluation.cedula}</td>
                  <td>{formatMoney(evaluation.requestedAmount)}</td>
                  <td>{evaluation.score}</td>
                  <td>
                    <span className={`status ${evaluation.status === 'APPROVED' ? 'approved' : 'rejected'}`}>
                      {evaluation.status === 'APPROVED' ? 'Aprobado' : 'Rechazado'}
                    </span>
                  </td>
                </tr>
              ))}
              {!evaluations.length && (
                <tr>
                  <td colSpan="5" className="empty-row">No hay evaluaciones registradas.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </main>
  );
}

/**
 * Formatea valores monetarios para Ecuador.
 *
 * @param {number|string} value valor numerico recibido desde la API.
 * @returns {string} monto en USD.
 */
function formatMoney(value) {
  return new Intl.NumberFormat('es-EC', {
    style: 'currency',
    currency: 'USD',
  }).format(Number(value));
}

/**
 * Formatea fechas ISO para lectura operativa.
 *
 * @param {string} value fecha ISO.
 * @returns {string} fecha local legible.
 */
function formatDate(value) {
  return new Intl.DateTimeFormat('es-EC', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(value));
}

createRoot(document.getElementById('root')).render(<App />);

