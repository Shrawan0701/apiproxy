# RateMate – API Limiter & Monitoring Platform

RateMate is a modern **API Limiter & Monitoring Platform** that helps developers **rate-limit, monitor, and analyze API requests with ease**. It is an **easy-to-use Postman alternative** with a clean dashboard, built-in authentication, and real-time analytics.  

## Features  

 - **API Rate Limiting** – Limit requests per user automatically.  
 - **Analytics Dashboard** – View request count, average latency, and detailed stats.  
 - **API Playground** – Make API calls directly from the dashboard, no need for Postman.  
 - **Secure API Keys** – Generate and manage API keys per user.  
 - **Auth & Access Control** – Only logged-in users can use your proxy & quota.  
 - **Fast & Simple** – Clean UI with blazing-fast backend.  
 - **Free Tier Ready** – 1000 requests/day per user (configurable).  

---

##  Tech Stack  

- **Frontend:** React
- **Backend:** Spring Boot + Spring Security + JWT Auth  
- **Database:** PostgreSQL
- **Redis** – In-memory data store for rate-limiting counters
- **Deployment:** Render (Backend) + Vercel (Frontend)  
- **Monitoring:** Custom built analytics service 

---

##  Using the Platform

- Sign Up / Log In → Create an account.
- Generate API Key → Get your personal key from the dashboard.
- Test API Calls → Use the playground to call any endpoint with your key.
- Monitor Analytics → Track requests, latency, and quota usage in real-time.

##  Example request

- curl -X POST https://apiproxy-zeak.onrender.com/api/proxy \
-  -H "Content-Type: application/json" \
-  -H "X-API-Key: YOUR_KEY_HERE" \
=  -d '{"url": "https://jsonplaceholder.typicode.com/posts", "method": "GET"}'

##  Why RateMate?

- All-in-One: No need for Postman, API keys, analytics tools separately.
- Developer Friendly: Lightweight, quick, and easy to set up.
- Secure: JWT-based auth + key-level request limits.
- Data Insights: Get latency, error rates, and request count in one place.
- Ready to Scale: Works locally or in production with no code changes.

##  Roadmap

- API Playground 
- Per-user rate limiting
- ⏳ Webhooks for analytics export
- ⏳ Slack/Email alerts for exceeded limits
- Google/Github login

