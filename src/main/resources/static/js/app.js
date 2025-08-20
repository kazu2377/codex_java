// Tiny ripple for buttons
document.addEventListener('click', function (e) {
  const btn = e.target.closest('.btn');
  if (!btn) return;
  const circle = document.createElement('span');
  const diameter = Math.max(btn.clientWidth, btn.clientHeight);
  const radius = diameter / 2;
  circle.style.width = circle.style.height = `${diameter}px`;
  const rect = btn.getBoundingClientRect();
  circle.style.left = `${e.clientX - rect.left - radius}px`;
  circle.style.top = `${e.clientY - rect.top - radius}px`;
  circle.classList.add('ripple');
  const ripple = btn.getElementsByClassName('ripple')[0];
  if (ripple) ripple.remove();
  btn.appendChild(circle);
}, false);

