const form = document.getElementById('stressForm');
const result = document.getElementById('result');
const loader = document.getElementById('loader');

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
        url: form.url.value,
        method: form.method.value,
        concurrency: Number(form.concurrency.value),
        durationSeconds: Number(form.durationSeconds.value),
        body: form.body.value || null
    };

    // Show loader and clear previous result
    loader.style.display = "block";
    result.innerHTML = "";

    try {
        const res = await fetch('/stress-test', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!res.ok) {
            throw new Error(`Server returned ${res.status}`);
        }

        const json = await res.json();

        // Color-code the verdict
        let verdict = json.verdict || "PENDING";
        let verdictHtml = `<span class="verdict-${verdict}">${verdict}</span>`;
        json.verdict = verdictHtml;

        // Convert JSON to pretty string with colorized verdict
        let jsonStr = JSON.stringify(json, null, 2)
            .replace(/"(verdict)": "(.*?)"/, (_, key, val) => {
                return `"${key}": ${val}`; // insert HTML for verdict
            });

        result.innerHTML = jsonStr;

    } catch (err) {
        result.textContent = "❌ Error: " + err;
    } finally {
        loader.style.display = "none"; // hide spinner when done
    }
});
