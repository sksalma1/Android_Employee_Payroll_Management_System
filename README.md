Road Accident Analysis & Prediction Using Weather Data This project aims to analyze and predict road accidents across the United States using weather and environmental data. It includes data engineering, machine learning, deep learning, and business intelligence components to provide meaningful insights and predictions.

📌 Project Overview Every year, thousands of road accidents occur due to changing weather conditions and environmental factors. By analyzing data from the US Accidents (2016–2021) dataset, this project explores patterns, builds predictive models, and visualizes findings using Power BI.

📁 Repository Structure

File/Folder Description trim.ipynb Trims the original dataset to 100,000 rows for faster local processing dwdm_india.ipynb Main notebook: includes data analysis, preprocessing, feature engineering, and model training dashboard.pdf Exported Power BI dashboard showcasing key insights and visualizations README.md Project overview and documentation (this file) Dataset Information Source: Kaggle - US Accidents (2016–2021) Size: ~7.7 million rows (trimmed to 100,000 for demo purposes)

🛠Tools & Technologies Python – Data processing and modeling PostgreSQL – For data storage and querying Jupyter Notebook – For EDA, feature engineering, and training ML models Scikit-learn, TensorFlow (Keras) – For ML and deep learning models Power BI – For interactive dashboards and visualizations Libraries: Pandas, NumPy, Matplotlib, Seaborn, Plotly, etc.

📌 Project Workflow Data Loading & Trimming Original dataset was too large (~6GB). Used trim.ipynb to select a balanced 100,000-row subset for local training and visualization. Data Preprocessing (in dwdm_india.ipynb) Handled missing values Selected important features related to weather and road layout Engineered synthetic non-accident data for binary classification Feature Sets Used Weather-Based: Temperature, Humidity, Visibility, Wind Speed, Precipitation Environmental-Based: Sunrise/Sunset, Junction, Traffic Signal, Roundabout

Machine Learning Models Random Forest Classifier: Captures non-linear relations and handles unscaled data Logistic Regression: Simple and interpretable baseline Deep Learning (Keras): Multi-layer perceptron with ReLU and sigmoid for binary output

Evaluation Used classification metrics like accuracy, precision, recall, and F1-score

Dashboard Creation Built a Power BI dashboard using exported summary data Insights include top states, streets, weather patterns, accident severity, and timezones

📈 Dashboard Highlights (Power BI) Top 10 Weather Conditions Causing Most Accidents → Rain, Fog, Snow, Haze, etc. Accidents by State and City → Interactive map showing hotspots (e.g., California, Texas, Florida) Accident Severity Distribution → bar charts visualizing severity levels Time Zone & Day/Night Distribution → Understand accident trends across different regions and times Top 10 States & Streets → Identifies most accident-prone locations Mixed Weather vs Day/Night Analysis → Checks risk patterns based on visibility and light conditions
