// La API se sirve desde el mismo Spring Boot en http://localhost:8080/students
const API_URL = '/students';
let students = [];
let editingId = null;

const $ = (id) => document.getElementById(id);
const formPanel = $('formPanel');
const form = $('studentForm');

async function apiRequest(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) }
  });
  const contentType = response.headers.get('content-type') || '';
  const data = contentType.includes('application/json') ? await response.json() : await response.text();
  if (!response.ok) {
    const detail = typeof data === 'object' ? (data.message || data.error || JSON.stringify(data)) : data;
    throw new Error(detail || `Error HTTP ${response.status}`);
  }
  return data;
}

async function loadStudents() {
  $('studentsTable').innerHTML = '<tr><td colspan="5" class="empty">Cargando estudiantes...</td></tr>';
  try {
    students = await apiRequest(API_URL);
    renderStudents();
    hideMessage();
  } catch (error) {
    students = [];
    renderStudents();
    showMessage(`No se pudo conectar con la API. ${error.message}. Verifica que Spring Boot esté ejecutándose.`, 'error');
  }
}

function renderStudents() {
  const term = $('searchInput').value.trim().toLowerCase();
  const filtered = students.filter(student =>
    [student.id, student.nombre, student.correo, student.numero_telefono, student.idioma]
      .some(value => String(value ?? '').toLowerCase().includes(term))
  );
  $('totalStudents').textContent = students.length;
  $('resultsCount').textContent = `${filtered.length} ${filtered.length === 1 ? 'estudiante' : 'estudiantes'}`;

  if (!filtered.length) {
    $('studentsTable').innerHTML = `<tr><td colspan="5" class="empty">${students.length ? 'No hay resultados para tu búsqueda.' : 'Todavía no hay estudiantes registrados.'}</td></tr>`;
    return;
  }

  $('studentsTable').innerHTML = filtered.map(student => `
    <tr>
      <td>#${escapeHtml(student.id)}</td>
      <td><div class="student-name">${escapeHtml(student.nombre)}</div><div class="student-email">${escapeHtml(student.correo)}</div></td>
      <td>${escapeHtml(student.numero_telefono)}</td>
      <td><span class="language">${escapeHtml(student.idioma)}</span></td>
      <td><div class="actions">
        <button class="small-btn" data-action="edit" data-id="${Number(student.id)}">Editar</button>
        <button class="small-btn delete" data-action="delete" data-id="${Number(student.id)}">Eliminar</button>
      </div></td>
    </tr>`).join('');
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, char => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[char]));
}

function openForm(student = null) {
  editingId = student ? student.id : null;
  $('formTitle').textContent = student ? `Editar estudiante #${student.id}` : 'Nuevo estudiante';
  $('saveBtn').textContent = student ? 'Guardar cambios' : 'Guardar estudiante';
  form.reset();
  if (student) {
    $('nombre').value = student.nombre || '';
    $('correo').value = student.correo || '';
    $('numero_telefono').value = student.numero_telefono || '';
    $('idioma').value = student.idioma || '';
  }
  formPanel.hidden = false;
  formPanel.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function closeForm() {
  formPanel.hidden = true;
  form.reset();
  editingId = null;
}

function showMessage(message, type = 'success') {
  const element = $('message');
  element.textContent = message;
  element.className = `message ${type}`;
  element.hidden = false;
}
function hideMessage() { $('message').hidden = true; }

form.addEventListener('submit', async event => {
  event.preventDefault();
  const payload = {
    nombre: $('nombre').value.trim(),
    correo: $('correo').value.trim(),
    numero_telefono: $('numero_telefono').value.trim(),
    idioma: $('idioma').value
  };
  if (!/^[0-9]{10}$/.test(payload.numero_telefono)) {
    showMessage('El teléfono debe contener exactamente 10 dígitos.', 'error');
    return;
  }
  const button = $('saveBtn');
  button.disabled = true;
  button.textContent = 'Guardando...';
  try {
    if (editingId !== null) {
      await apiRequest(`${API_URL}/${editingId}`, { method: 'PUT', body: JSON.stringify(payload) });
      showMessage('Estudiante actualizado correctamente.');
    } else {
      await apiRequest(API_URL, { method: 'POST', body: JSON.stringify(payload) });
      showMessage('Estudiante registrado correctamente.');
    }
    closeForm();
    await loadStudents();
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    button.disabled = false;
    button.textContent = editingId !== null ? 'Guardar cambios' : 'Guardar estudiante';
  }
});

$('studentsTable').addEventListener('click', async event => {
  const button = event.target.closest('button[data-action]');
  if (!button) return;
  const id = Number(button.dataset.id);
  const student = students.find(item => Number(item.id) === id);
  if (button.dataset.action === 'edit' && student) openForm(student);
  if (button.dataset.action === 'delete' && student) {
    if (!confirm(`¿Seguro que deseas eliminar a ${student.nombre}?`)) return;
    try {
      await apiRequest(`${API_URL}/${id}`, { method: 'DELETE' });
      showMessage('Estudiante eliminado correctamente.');
      await loadStudents();
    } catch (error) { showMessage(error.message, 'error'); }
  }
});

$('newStudentBtn').addEventListener('click', () => openForm());
$('closeFormBtn').addEventListener('click', closeForm);
$('cancelBtn').addEventListener('click', closeForm);
$('refreshBtn').addEventListener('click', loadStudents);
$('searchInput').addEventListener('input', renderStudents);
loadStudents();
