
const $ = id => document.getElementById(id);

// Convert database values into safe HTML text
function escapeHTML(value) {
  return String(value ?? "").replace(/[&<>"']/g, char => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;"
  })[char]);
}

// Search student when the form is submitted
$("form").addEventListener("submit", async function(e) {
  e.preventDefault();

  const identifier = $("id").value.trim().toUpperCase();
  const dob = $("dob").value;

  $("err").textContent = "";

  if (!identifier || !dob) {
    $("err").textContent = "Please enter your number and date of birth.";
    return;
  }

  // Check whether the user entered a registration number or roll number
  const params = new URLSearchParams();

  if (identifier.startsWith("REG-")) {
    params.append("registrationNo", identifier);
  } else {
    params.append("rollNo", identifier);
  }

  params.append("dob", dob);

  try {
    const response = await fetch(`/api/student?${params.toString()}`);
    const data = await response.json();

    if (!response.ok || data.error) {
      $("err").textContent = data.error || "Something went wrong.";
      return;
    }

    showStudent(data);

  } catch (error) {
    $("err").textContent =
      "Unable to connect to the server. Please try again.";
    console.error(error);
  }
});

// Display student information
function showStudent(data) {
  const s = data.student;
  const fee = data.fees;

  $("home").style.display = "none";
  $("result").style.display = "block";

  // Student profile
  $("av").textContent = s.name ? s.name.charAt(0) : "?";
  $("nm").textContent = s.name;
  $("info").textContent =
    `${s.rollNo} | ${s.registrationNo} | ${s.course}, Semester ${s.semester}`;

  // Fee details
  if (fee) {
    const total = Number(fee.totalFee);
    const paid = Number(fee.paid);
    const due = total - paid;
    const percentage = total > 0 ? (paid / total) * 100 : 0;

    $("ft").textContent = "₹" + total.toLocaleString("en-IN");
    $("fp").textContent = "₹" + paid.toLocaleString("en-IN");
    $("fd").textContent = "₹" + due.toLocaleString("en-IN");
    $("bar").style.width = Math.min(100, Math.max(0, percentage)) + "%";
  } else {
    $("ft").textContent = "No record";
    $("fp").textContent = "No record";
    $("fd").textContent = "No record";
    $("bar").style.width = "0%";
  }

  // Semester marks
  displayMarks(data.marks || []);

  // Assignments
  displayAssignments(data.assignments || []);
}

// Display marks semester-wise
function displayMarks(marks) {
  const tabs = $("tabs");
  const table = $("mk");

  tabs.innerHTML = "";
  table.innerHTML = "";

  if (marks.length === 0) {
    table.innerHTML = '<tr><td colspan="4">No marks available.</td></tr>';
    return;
  }

  const semesters = [...new Set(marks.map(m => m.semester))]
    .sort((a, b) => a - b);

  semesters.forEach((semester, index) => {
    const button = document.createElement("button");
    button.type = "button";
    button.textContent = "Semester " + semester;
    button.className = index === 0 ? "tab active" : "tab";

    button.addEventListener("click", () => {
      tabs.querySelectorAll("button").forEach(btn =>
        btn.classList.remove("active")
      );

      button.classList.add("active");
      showSemesterMarks(marks, semester);
    });

    tabs.appendChild(button);
  });

  showSemesterMarks(marks, semesters[0]);
}

// Show marks for the selected semester
function showSemesterMarks(marks, semester) {
  const selectedMarks = marks.filter(m => m.semester === semester);

  $("mk").innerHTML = selectedMarks.map(m => `
    <tr>
      <td>${escapeHTML(m.subject)}</td>
      <td>${escapeHTML(m.marksObtained)}</td>
      <td>${escapeHTML(m.maxMarks)}</td>
      <td>${Number(m.marksObtained) >= 40 ? "Pass" : "Fail"}</td>
    </tr>
  `).join("");
}

// Display assignments
function displayAssignments(assignments) {
  if (assignments.length === 0) {
    $("as").innerHTML =
      '<tr><td colspan="4">No assignments available.</td></tr>';
    return;
  }

  $("as").innerHTML = assignments.map(a => {
    const dueDate = a.dueDate
      ? new Date(a.dueDate + "T00:00:00").toLocaleDateString("en-IN")
      : "-";

    const status = String(a.status || "").toUpperCase();

    return `
      <tr>
        <td>${escapeHTML(a.subject)}</td>
        <td>${escapeHTML(a.title)}</td>
        <td>${dueDate}</td>
        <td>
          <span class="badge ${status === "DONE" ? "done" : "pending"}">
            ${escapeHTML(status)}
          </span>
        </td>
      </tr>
    `;
  }).join("");
}

// Search again button
$("back").addEventListener("click", () => {
  $("result").style.display = "none";
  $("home").style.display = "grid";
  $("err").textContent = "";
});
