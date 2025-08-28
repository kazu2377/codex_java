// フッタの年号
document.getElementById('y').textContent = new Date().getFullYear();

// モバイルナビ開閉
const navBtn = document.querySelector('[data-nav-toggle]');
const nav = document.querySelector('[data-nav]');
if (navBtn && nav) {
  navBtn.addEventListener('click', () => {
    const open = nav.classList.toggle('is-open');
    navBtn.setAttribute('aria-expanded', String(open));
  });
  nav.querySelectorAll('a').forEach(a => a.addEventListener('click', () => {
    nav.classList.remove('is-open');
    navBtn.setAttribute('aria-expanded', 'false');
  }));
}

// スムーススクロール
document.querySelectorAll('a[href^="#"]').forEach(a => {
  a.addEventListener('click', e => {
    const id = a.getAttribute('href');
    if (!id || id === '#') return;
    const el = document.querySelector(id);
    if (el) {
      e.preventDefault();
      const top = el.getBoundingClientRect().top + window.pageYOffset - 64;
      window.scrollTo({ top, behavior: 'smooth' });
      history.pushState(null, '', id);
    }
  });
});

// スクロール出現
const io = new IntersectionObserver(entries => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('is-visible');
      io.unobserve(entry.target);
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.reveal').forEach(el => io.observe(el));

// 画像差し替え用: hero画像を簡単に変更する
// /assets/hero.jpg を設置したら下記は不要。CSS の var(--hero-image) を上書きする例。
// document.documentElement.style.setProperty('--hero-image', "url('assets/your-hero.jpg')");

// デモ送信（実装時は fetch に置換）
const form = document.querySelector('[data-contact-form]');
if (form) {
  form.addEventListener('submit', e => {
    e.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    alert('デモ送信\n' + JSON.stringify(data, null, 2));
  });
}

