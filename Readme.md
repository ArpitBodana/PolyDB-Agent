# PolyDB Agent

PolyDB Agent is an intelligent, agentic Spring Boot application powered by **Spring AI**. It leverages Large Language Models (LLMs) to understand natural language prompts and seamlessly execute backend operations, including managing database records (Users and Tasks) and exporting data into various formats.

## 🚀 Features

### 1. Natural Language Interface
The application exposes a conversational AI endpoint that interprets user requests and orchestrates the appropriate background tools to fulfill them.

### 2. User Management Agent Tools
The AI agent has access to a comprehensive suite of tools to manage User data in PostgreSQL:
- `create_user`: Create new users with name, email, age, and city.
- `get_all_users`: Fetch the complete list of users.
- `get_user_by_id` / `get_user_by_email`: Fetch specific users.
- `get_users_by_city`: Filter users by location.
- `update_user` / `delete_user`: Modify or remove existing users.

### 3. Task Management Agent Tools
The agent can autonomously manage project or system tasks:
- `create_task`: Create tasks assigning title, description, priority, assignee, and status.
- `get_all_tasks`: Retrieve all tasks.
- `get_task_by_id`: Fetch a specific task.
- `get_tasks_by_status` / `get_tasks_by_priority`: Filter tasks based on specific criteria.
- `update_task` / `delete_task`: Modify or delete tasks.

### 4. Smart Data Export
The agent integrates with a Generic Export Service, allowing users to ask the AI to export queried data:
- `export_task_data`: Export queried tasks into CSV, Excel, or PDF.
- `export_user_data`: Export queried users into CSV, Excel, or PDF.

## ⚙️ Technical Details & Architecture

PolyDB Agent is built on a modern, high-performance Java stack utilizing the latest Spring ecosystems and Model Context Protocol (MCP) concepts.

### Core Stack
* **Java**: Version 21
* **Spring Boot**: Version 3.5.14
* **Spring AI**: Version 1.1.6
* **Build Tool**: Maven

### Architectural Highlights
* **Model Context Protocol (MCP) Integration**: The application utilizes the `spring-ai-starter-mcp-client` dependency. This modularizes the architecture, allowing the core agent to communicate with specialized tools (like PostgreSQL operations and Data Export functions) using standardized MCP patterns.
* **LLM Engine**: Configured by default to use OpenAI via `spring-ai-starter-model-openai` (though architected to easily swap to Ollama or other providers).
* **Observability**: Integrates `spring-boot-starter-actuator` to expose operational metrics, health checks, and application monitoring endpoints.
* **Database**: PostgreSQL for robust relational data storage (User and Task entities).
* **Code Optimization**: Uses **Lombok** to eliminate boilerplate code (Getters, Setters, Builders, Constructors) across services and entities.

## 🔌 API Endpoints

### Query the AI Agent
Interact with the Poly DBAgent using natural language queries.

**HTTP Request**
`GET /agent/query?query={your_prompt}`

**Example Requests:**
- `GET /agent/query?query=Create a new high priority task for John Doe to fix the login bug.`
- `GET /agent/query?query=Get all users living in New York and export the data to a PDF named ny_users.`
- `GET /agent/query?query=Delete the task with ID 5.`

## 📦 Project Structure Overview
The application separates standard logic from LLM tool definitions:
* `com.absys.io.postgresql_mcp.tools.*`: Contains AI tools for database operations (`UserTools`, `TaskTools`).
* `com.absys.io.export_data_mcp.tools.*`: Contains AI tools for file generation (`GenericExportTool`).
* `com.absys.io.polydb.agent.controller.*`: Exposes the AI chat endpoints (`AiController`).

## 🛠️ Setup & Installation

1. Clone the repository.
2. Ensure you have a running PostgreSQL instance and configure your `application.yml` or `application.properties` with your database credentials.
3. Provide your OpenAI API key in your Spring Boot properties:
   ```properties
   spring.ai.openai.api-key=YOUR_API_KEY