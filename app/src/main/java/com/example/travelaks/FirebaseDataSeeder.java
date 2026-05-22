package com.example.travelaks;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDataSeeder {

    private static final String TAG = "FirebaseDataSeeder";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface Callback {
        void onComplete(boolean success, String message);
    }

    public static void seedAll(Callback callback) {
        db.collection("cities").limit(1).get()
            .addOnSuccessListener(snap -> {
                if (!snap.isEmpty()) {
                    callback.onComplete(false, "Data already exists. Delete collections first to re-seed.");
                    return;
                }
                seedCities(() ->
                    seedHotels(() ->
                        seedAttractions(() ->
                            seedActivities(() ->
                                seedFaqs(() ->
                                    callback.onComplete(true, "All data seeded successfully!")
                                )
                            )
                        )
                    )
                );
            })
            .addOnFailureListener(e -> callback.onComplete(false, "Check failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // CITIES
    // ─────────────────────────────────────────────
    private static void seedCities(Runnable next) {
        WriteBatch batch = db.batch();

        batch.set(db.collection("cities").document("riyadh"), city(
            "Riyadh", "The capital city of Saudi Arabia, blending modern skyscrapers with rich cultural heritage and ancient history.",
            "riyadh", "Saudi Arabia", "Central", 24.7136, 46.6753));

        batch.set(db.collection("cities").document("jeddah"), city(
            "Jeddah", "A vibrant port city on the Red Sea coast, known for its UNESCO-listed historic district Al-Balad and stunning corniche.",
            "jeddh", "Saudi Arabia", "Western", 21.5433, 39.1728));

        batch.set(db.collection("cities").document("makkah"), city(
            "Makkah", "The holiest city in Islam, home to Masjid Al-Haram and the Kaaba, and the destination of the annual Hajj pilgrimage.",
            "makkah", "Saudi Arabia", "Hejaz", 21.3891, 39.8579));

        batch.set(db.collection("cities").document("madinah"), city(
            "Madinah", "The second holiest city in Islam, home to Al-Masjid an-Nabawi, the mosque of the Prophet Muhammad (PBUH).",
            "almadina", "Saudi Arabia", "Hejaz", 24.5247, 39.5692));

        batch.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, "Cities seeded"); next.run(); })
            .addOnFailureListener(e -> Log.e(TAG, "Cities failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // HOTELS
    // ─────────────────────────────────────────────
    private static void seedHotels(Runnable next) {
        WriteBatch batch = db.batch();

        // ── Riyadh ──
        addHotel(batch, "Bab Samhan Diriyah", "riyadh", "Riyadh", 4.7, "5-star Heritage",
            "+966 11 820 8888",
            "A UNESCO-adjacent luxury hotel within the Luxury Collection, set in historic Diriyah. Combines traditional Najdi architecture with world-class amenities.",
            "bab_samhan", 1800, 24.7333, 46.5728);

        addHotel(batch, "The St. Regis Riyadh", "riyadh", "Riyadh", 4.6, "5-star",
            "+966 11 820 0000",
            "A modern luxury hotel in the Riyadh Via area with signature Butler service, multiple international restaurants, and a rooftop pool.",
            "stregis", 1500, 24.7052, 46.6869);

        addHotel(batch, "The Ritz-Carlton Riyadh", "riyadh", "Riyadh", 4.6, "5-star Palace",
            "+966 11 802 8020",
            "A magnificent palace-hotel set on 52 acres of lush gardens, featuring fine dining, a world-class spa, and an indoor pool.",
            "ritz_carlton", 1600, 24.6859, 46.7095);

        addHotel(batch, "Four Seasons Hotel Riyadh", "riyadh", "Riyadh", 4.6, "5-star",
            "+966 11 211 5000",
            "Perched in Kingdom Centre Tower with panoramic city views, offering a women-only floor, luxury spa, and exquisite dining.",
            "four_seasons", 1700, 24.7129, 46.6740);

        addHotel(batch, "Mandarin Oriental Al Faisaliah", "riyadh", "Riyadh", 4.5, "5-star",
            "+966 11 273 2000",
            "Located in Al Faisaliah Tower, offering an elegant stay with a signature spa, 3 gourmet restaurants, and airport shuttle.",
            "mandarin", 1400, 24.7054, 46.6831);

        addHotel(batch, "Hyatt Regency Riyadh Olaya", "riyadh", "Riyadh", 4.4, "5-star Business",
            "+966 11 288 1234",
            "A modern business hotel in a sleek tower with floor-to-ceiling windows, extensive meeting facilities, and Olaya street views.",
            "hyatt_regency", 1100, 24.7202, 46.6819);

        // ── Jeddah ──
        addHotel(batch, "Park Hyatt Jeddah", "jeddah", "Jeddah", 4.7, "5-star Marina",
            "+966 12 653 1234",
            "A stunning waterfront hotel on the Corniche Marina with direct Red Sea access, private beach, and celebrated seafood dining.",
            "jeddh", 1600, 21.5186, 39.1597);

        addHotel(batch, "Waldorf Astoria Jeddah", "jeddah", "Jeddah", 4.6, "5-star",
            "+966 12 638 8888",
            "Iconic luxury overlooking the Red Sea, featuring the acclaimed Peacock Alley lounge, rooftop pool, and butler service.",
            "jeddh", 1500, 21.5383, 39.1725);

        addHotel(batch, "Four Seasons Hotel Jeddah", "jeddah", "Jeddah", 4.6, "5-star",
            "+966 12 270 3800",
            "Situated on the Corniche with sweeping Red Sea panoramas, world-class spa, private beach, and three acclaimed restaurants.",
            "jeddh", 1700, 21.5267, 39.1522);

        addHotel(batch, "Rosewood Jeddah", "jeddah", "Jeddah", 4.5, "5-star",
            "+966 12 290 0011",
            "A design-forward hotel offering Red Sea views, a rooftop infinity pool, and an immersive wellness spa experience.",
            "jeddh", 1300, 21.5297, 39.1579);

        addHotel(batch, "JW Marriott Hotel Jeddah", "jeddah", "Jeddah", 4.4, "5-star",
            "+966 12 699 9999",
            "A contemporary hotel in North Jeddah, known for its expansive rooms, impressive fitness center, and proximity to shopping districts.",
            "jeddh", 1000, 21.5439, 39.1736);

        // ── Makkah ──
        addHotel(batch, "Fairmont Makkah Clock Royal Tower", "makkah", "Makkah", 4.8, "5-star",
            "+966 12 577 0000",
            "The crown jewel of Makkah hotels, located in the iconic Abraj Al-Bait towers directly overlooking Masjid Al-Haram with unmatched views of the Kaaba.",
            "makkah", 2000, 21.4187, 39.8229);

        addHotel(batch, "Raffles Makkah Palace", "makkah", "Makkah", 4.7, "5-star",
            "+966 12 577 3333",
            "An ultra-luxurious palace hotel in Abraj Al-Bait with spacious suites, dedicated Haram views, and exceptional butler service.",
            "makkah", 1900, 21.4178, 39.8225);

        addHotel(batch, "Swissôtel Al Maqam Makkah", "makkah", "Makkah", 4.6, "5-star",
            "+966 12 577 5555",
            "A premier hotel steps from the Grand Mosque, offering large rooms, multiple dining options, and seamless Umrah convenience.",
            "makkah", 1500, 21.4186, 39.8233);

        addHotel(batch, "Hilton Makkah Convention Hotel", "makkah", "Makkah", 4.5, "5-star",
            "+966 12 571 1111",
            "A pilgrimage-focused hotel with comfortable rooms, Haram shuttle service, and comprehensive amenities for religious guests.",
            "makkah", 1200, 21.4139, 39.8244);

        addHotel(batch, "Jabal Omar Hyatt Regency Makkah", "makkah", "Makkah", 4.5, "5-star",
            "+966 12 503 0000",
            "Located in Jabal Omar development, offering stunning Kaaba views, world-class dining, and direct access to the Grand Mosque via walkway.",
            "makkah", 1400, 21.4236, 39.8292);

        // ── Madinah ──
        addHotel(batch, "Anwar Al Madinah Mövenpick Hotel", "madinah", "Madinah", 4.6, "5-star",
            "+966 14 828 5555",
            "A premium hotel steps from Al-Masjid an-Nabawi with panoramic views of the Prophet's Mosque, elegant rooms, and fine dining.",
            "almadina", 1100, 24.4680, 39.6113);

        addHotel(batch, "Sheraton Al Noor Madinah Hotel", "madinah", "Madinah", 4.5, "5-star",
            "+966 14 828 9999",
            "A distinguished hotel near Al-Masjid an-Nabawi offering spacious suites, multiple restaurants, and a dedicated pilgrimage concierge.",
            "almadina", 1000, 24.4731, 39.6108);

        addHotel(batch, "Hilton Madinah", "madinah", "Madinah", 4.5, "5-star",
            "+966 14 848 0000",
            "Situated close to the Prophet's Mosque, featuring comfortable rooms, an outdoor pool, and seamless transport for spiritual visitors.",
            "almadina", 950, 24.4664, 39.6100);

        addHotel(batch, "Pullman Zamzam Madinah", "madinah", "Madinah", 4.4, "5-star",
            "+966 14 825 8787",
            "A contemporary hotel offering Nabawi Mosque views, modern facilities, and a variety of dining options for pilgrims and tourists.",
            "almadina", 900, 24.4692, 39.6120);

        addHotel(batch, "Dar Al Iman InterContinental Madinah", "madinah", "Madinah", 4.4, "5-star",
            "+966 14 826 0000",
            "A refined hotel combining tradition with modernity, minutes from Al-Masjid an-Nabawi, with a rooftop restaurant and prayer facilities.",
            "almadina", 880, 24.4697, 39.6112);

        batch.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, "Hotels seeded"); next.run(); })
            .addOnFailureListener(e -> Log.e(TAG, "Hotels failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // ATTRACTIONS
    // ─────────────────────────────────────────────
    private static void seedAttractions(Runnable next) {
        WriteBatch batch = db.batch();

        // ── Riyadh ──
        addAttraction(batch, "At-Turaif District", "riyadh", "Riyadh", 4.7, "UNESCO Heritage",
            "The birthplace of the First Saudi State and a UNESCO World Heritage Site, showcasing magnificent Najdi mud-brick architecture in the historic Diriyah district.",
            "turaifddistrict", "historical", 24.7361, 46.5707);

        addAttraction(batch, "Al Masmak Fort", "riyadh", "Riyadh", 4.6, "Historic Fort",
            "A 19th-century mud-brick fortress that played a pivotal role in the unification of Saudi Arabia. Home to a museum with artifacts from the founding of the Kingdom.",
            "al_masmak_fort", "historical", 24.6875, 46.7133);

        addAttraction(batch, "National Museum of Saudi Arabia", "riyadh", "Riyadh", 4.6, "Museum",
            "An interactive eight-gallery museum tracing the history and culture of Saudi Arabia from pre-Islamic times through the modern era with immersive exhibits.",
            "national_museum", "cultural", 24.6884, 46.7127);

        addAttraction(batch, "Kingdom Centre Tower", "riyadh", "Riyadh", 4.5, "Modern Landmark",
            "An iconic 300-metre skyscraper housing the Four Seasons Hotel, luxury shopping, and a sky bridge at the top offering breathtaking 360° views of Riyadh.",
            "riyadh", "modern", 24.7129, 46.6740);

        addAttraction(batch, "King Abdulaziz Historical Center", "riyadh", "Riyadh", 4.4, "Cultural Complex",
            "A sprawling cultural complex comprising the National Museum, Murabba Palace, and lush gardens — a tribute to the founder of modern Saudi Arabia.",
            "riyadh", "cultural", 24.6878, 46.7120);

        addAttraction(batch, "Murabba Palace", "riyadh", "Riyadh", 4.3, "Historic Palace",
            "The residence of King Abdulaziz Al Saud in the 1930s, now a preserved museum displaying royal artifacts, furniture, and photographs of early Riyadh.",
            "riyadh", "historical", 24.6886, 46.7113);

        // ── Jeddah ──
        addAttraction(batch, "Al-Balad Historic District", "jeddah", "Jeddah", 4.8, "UNESCO Heritage",
            "The ancient walled city of Jeddah, a UNESCO World Heritage Site famous for its centuries-old coral-stone buildings adorned with intricately carved wooden balconies.",
            "jeddh", "historical", 21.4858, 39.1889);

        addAttraction(batch, "King Fahd Fountain", "jeddah", "Jeddah", 4.7, "Landmark",
            "The world's tallest fountain, shooting water 312 metres into the sky over the Red Sea. Illuminated at night with 500 spotlights — a spectacular Jeddah icon.",
            "jeddh", "modern", 21.5186, 39.1450);

        addAttraction(batch, "Al-Rahma Floating Mosque", "jeddah", "Jeddah", 4.7, "Religious Site",
            "An ethereal mosque built on stilts over the Red Sea, appearing to float at high tide. One of the most photographed landmarks on the Jeddah Corniche.",
            "jeddh", "religious", 21.4703, 39.1575);

        addAttraction(batch, "Jeddah Corniche", "jeddah", "Jeddah", 4.5, "Waterfront Promenade",
            "A 30-km coastal promenade stretching along the Red Sea, lined with parks, sculptures, restaurants, and stunning sunset views of the King Fahd Fountain.",
            "jeddh", "natural", 21.5433, 39.1480);

        addAttraction(batch, "Jeddah Sculpture Museum", "jeddah", "Jeddah", 4.3, "Outdoor Museum",
            "An open-air sculpture park along the Corniche featuring over 400 artworks by renowned international and Arab artists, free to visit at any time.",
            "jeddh", "cultural", 21.5500, 39.1450);

        // ── Makkah ──
        addAttraction(batch, "Masjid Al-Haram", "makkah", "Makkah", 5.0, "Holy Mosque",
            "The largest mosque in the world and the holiest site in Islam, surrounding the Kaaba — the focal point of Muslim prayer worldwide. Capable of holding over 4 million worshippers.",
            "makkah", "religious", 21.4225, 39.8262);

        addAttraction(batch, "Cave of Hira (Jabal Al-Nour)", "makkah", "Makkah", 4.8, "Religious Site",
            "A mountain where the Prophet Muhammad (PBUH) received the first revelation of the Quran. A sacred climb of 1,750 steps leads to the historic cave.",
            "makkah", "religious", 21.4578, 39.8619);

        addAttraction(batch, "Mount Arafat (Jabal Arafat)", "makkah", "Makkah", 4.8, "Religious Site",
            "The most significant stop in the Hajj pilgrimage. Pilgrims gather here on the 9th of Dhul Hijjah for the Day of Arafah, seeking forgiveness and blessings.",
            "makkah", "religious", 21.3547, 39.9844);

        addAttraction(batch, "Abraj Al-Bait (Makkah Royal Clock Tower)", "makkah", "Makkah", 4.6, "Modern Landmark",
            "A massive complex of seven towers including the world's largest clock tower. The clock face is visible from 25km away and the complex houses hotels, malls, and a museum.",
            "makkah", "modern", 21.4187, 39.8229);

        addAttraction(batch, "Masjid al-Khaif, Mina", "makkah", "Makkah", 4.5, "Religious Site",
            "One of the largest mosques in the world located in Mina, historically significant during the Hajj season. Pilgrims offer prayers here after the stoning ritual.",
            "makkah", "religious", 21.4133, 39.8944);

        // ── Madinah ──
        addAttraction(batch, "Al-Masjid an-Nabawi", "madinah", "Madinah", 5.0, "Prophet's Mosque",
            "The second holiest site in Islam, built by the Prophet Muhammad (PBUH) himself. Houses his tomb and draws millions of visitors year-round for prayer and spiritual reflection.",
            "almadina", "religious", 24.4672, 39.6111);

        addAttraction(batch, "Quba Mosque", "madinah", "Madinah", 4.8, "Historic Mosque",
            "The first mosque ever built in Islam, founded by the Prophet Muhammad (PBUH) upon his arrival in Madinah. Praying two rak'ahs here equals the reward of an Umrah.",
            "almadina", "religious", 24.4400, 39.6178);

        addAttraction(batch, "Mount Uhud", "madinah", "Madinah", 4.7, "Historic Site",
            "The site of the famous Battle of Uhud in 625 CE. The mountain holds great religious significance, with the graves of the martyrs including Hamza ibn Abdul-Muttalib.",
            "almadina", "historical", 24.5050, 39.6278);

        addAttraction(batch, "Al-Masjid al-Qiblatayn", "madinah", "Madinah", 4.6, "Historic Mosque",
            "The Mosque of the Two Qiblas, where the direction of prayer was changed from Jerusalem to Makkah during the time of the Prophet. A unique and revered site in Islamic history.",
            "almadina", "religious", 24.4819, 39.5903);

        addAttraction(batch, "Al-Baqi Cemetery", "madinah", "Madinah", 4.5, "Historic Cemetery",
            "The oldest and most sacred cemetery in Madinah, the burial ground for thousands of the Prophet's companions, family members, and early Muslims.",
            "almadina", "historical", 24.4664, 39.6144);

        batch.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, "Attractions seeded"); next.run(); })
            .addOnFailureListener(e -> Log.e(TAG, "Attractions failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // ACTIVITIES
    // ─────────────────────────────────────────────
    private static void seedActivities(Runnable next) {
        WriteBatch batch = db.batch();

        // ── Riyadh ──
        addActivity(batch, "Prison Island Riyadh", "riyadh", "Riyadh", 4.8, "Interactive Adventure",
            "An immersive indoor adventure center with group challenges, puzzle-filled dungeons, and escape room scenarios designed for teams and families.",
            "prison_island", "4 PM", "12 AM", "Park Avenue Mall, Cordoba District", 24.7381, 46.8258);

        addActivity(batch, "Boulevard World", "riyadh", "Riyadh", 4.7, "Entertainment Hub",
            "A sprawling 230,000 sqm entertainment destination featuring 12 themed zones inspired by world cities, live shows, restaurants, and family rides.",
            "boulevard_world", "4 PM", "12 AM", "Al Aqiq District, North Riyadh", 24.8081, 46.7372);

        addActivity(batch, "Laser Tag Arena", "riyadh", "Riyadh", 4.4, "Multiplayer Action",
            "A high-tech indoor multiplayer arena with safe laser weapons, multi-level obstacles, and atmospheric special lighting for teams of all ages.",
            "laser", "3 PM", "11 PM", "Al Olaya District, Riyadh", 24.7202, 46.6819);

        addActivity(batch, "Riyadh Zoo", "riyadh", "Riyadh", 4.2, "Family Attraction",
            "Home to over 1,500 animal species across spacious enclosures. Features an internal train ride, aviaries, and reptile exhibits for a full family day out.",
            "riyadh_zoo", "8 AM", "4:30 PM", "Al Malaz District, Riyadh", 24.6856, 46.7367);

        addActivity(batch, "Snow City Riyadh", "riyadh", "Riyadh", 4.0, "Ice Adventure",
            "Experience real snow, ice skating, and mini ski slopes in the heart of Riyadh — a unique indoor winter wonderland perfect for families and children.",
            "snow_city", "10 AM", "12 AM", "Al Othaim Mall, Al Rabwah", 24.7683, 46.6767);

        addActivity(batch, "VR Park Riyadh", "riyadh", "Riyadh", 4.5, "Virtual Reality",
            "Saudi Arabia's premier virtual reality entertainment center offering cutting-edge VR experiences, rides, and games in a futuristic setting.",
            "riyadh", "2 PM", "11 PM", "Riyadh Front, King Salman Road", 24.7500, 46.7100);

        // ── Jeddah ──
        addActivity(batch, "Fakieh Aquarium", "jeddah", "Jeddah", 4.5, "Aquarium & Sea Show",
            "Jeddah's premier marine attraction with over 200 species of Red Sea fish, dolphin and sea lion shows, a waterpark, and an amusement area for the whole family.",
            "jeddh", "10 AM", "10 PM", "Al Shati District, North Jeddah", 21.5439, 39.1250);

        addActivity(batch, "Al-Shallal Theme Park", "jeddah", "Jeddah", 4.6, "Theme Park",
            "A comprehensive family entertainment complex featuring roller coasters, bumper cars, an ice skating rink, cinema, and a waterpark on the Red Sea coast.",
            "jeddh", "3 PM", "12 AM", "Al Rawdah District, Jeddah", 21.5861, 39.1300);

        addActivity(batch, "Red Sea Scuba Diving", "jeddah", "Jeddah", 4.8, "Water Sport",
            "Explore one of the world's richest coral reef ecosystems in the Red Sea. Certified centers offer beginner and advanced dives with stunning marine biodiversity.",
            "jeddh", "7 AM", "5 PM", "Jeddah Marine Clubs, South Corniche", 21.4925, 39.1741);

        addActivity(batch, "Al-Balad Night Walking Tour", "jeddah", "Jeddah", 4.7, "Cultural Tour",
            "A guided evening stroll through UNESCO-listed Al-Balad, exploring ancient coral buildings, traditional souqs, spice markets, and lantern-lit alleyways.",
            "jeddh", "5 PM", "11 PM", "Al-Balad Historic District, Jeddah", 21.4858, 39.1889);

        addActivity(batch, "Jeddah Waterfront Walk", "jeddah", "Jeddah", 4.4, "Leisure Walk",
            "Enjoy a scenic walk along the 30-km Jeddah Corniche at sunset, with parks, sculptures, cafes, and a front-row view of the King Fahd Fountain lit at night.",
            "jeddh", "6 AM", "12 AM", "Jeddah Corniche Promenade", 21.5433, 39.1480);

        // ── Makkah ──
        addActivity(batch, "Abraj Al-Bait Observation Deck", "makkah", "Makkah", 4.5, "Panoramic View",
            "Ascend to the observation deck of the Makkah Royal Clock Tower for a breathtaking 360° view of Masjid Al-Haram, the Kaaba, and the entire holy city.",
            "makkah", "10 AM", "11 PM", "Abraj Al-Bait Towers, Central Makkah", 21.4187, 39.8229);

        addActivity(batch, "Souq Al-Zahed Shopping", "makkah", "Makkah", 4.3, "Traditional Market",
            "A vibrant traditional market near the Grand Mosque offering prayer beads, Islamic art, dates, Zamzam water containers, and perfumes at competitive prices.",
            "makkah", "9 AM", "12 AM", "Near Masjid Al-Haram, Central Makkah", 21.4167, 39.8194);

        addActivity(batch, "Makkah Museum (Al-Zaher Palace)", "makkah", "Makkah", 4.4, "Museum",
            "A historical museum housed in a former royal palace, displaying rare artifacts, old photographs, maps, and documents tracing the history of Makkah and the Grand Mosque.",
            "makkah", "9 AM", "6 PM", "Ajyad Neighborhood, Central Makkah", 21.4200, 39.8250);

        // ── Madinah ──
        addActivity(batch, "Madinah Date Market Tour", "madinah", "Madinah", 4.6, "Cultural Experience",
            "Explore the world-famous date markets of Madinah and taste over 100 varieties of premium dates. Local vendors offer free samples and package deals for visitors.",
            "almadina", "8 AM", "10 PM", "Central Market Area, Madinah", 24.4697, 39.6100);

        addActivity(batch, "Al-Hejaz Railway Museum", "madinah", "Madinah", 4.5, "Historic Museum",
            "A fascinating museum at the old Hejaz Railway station, showcasing original Ottoman-era locomotives, carriages, and documents from the historic Hejaz Railway line.",
            "almadina", "9 AM", "5 PM", "Old Train Station, Central Madinah", 24.4708, 39.5861);

        addActivity(batch, "Mount Uhud Guided Visit", "madinah", "Madinah", 4.7, "Historical Tour",
            "A guided historical tour of the Battle of Uhud site, visiting the burial ground of martyrs, the Archer's Hill, and key landmarks with expert Islamic history commentary.",
            "almadina", "8 AM", "6 PM", "Uhud Mountain, North Madinah", 24.5050, 39.6278);

        addActivity(batch, "Quba Mosque Visit & Prayer", "madinah", "Madinah", 4.8, "Spiritual Visit",
            "Visit the first mosque built in Islam. Praying two rak'ahs here earns the reward equivalent to Umrah. The mosque features beautiful white marble architecture.",
            "almadina", "5 AM", "11 PM", "Quba District, South Madinah", 24.4400, 39.6178);

        batch.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, "Activities seeded"); next.run(); })
            .addOnFailureListener(e -> Log.e(TAG, "Activities failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // FAQS
    // ─────────────────────────────────────────────
    private static void seedFaqs(Runnable next) {
        WriteBatch batch = db.batch();

        addFaq(batch, 1, "What is Travel SKA?",
            "Travel SKA is an AI-powered travel guide for Saudi Arabia (KSA). It helps you explore top cities, discover hotels, tourist attractions, and activities, and get instant answers from our AI chat assistant.");

        addFaq(batch, 2, "Which cities are currently covered?",
            "Travel SKA currently covers four major Saudi cities: Riyadh (capital), Jeddah (Red Sea coast), Makkah (holiest city in Islam), and Madinah (second holiest city in Islam). More cities are coming soon.");

        addFaq(batch, 3, "How does the AI Chat feature work?",
            "Select any city and tap 'Ask AI'. You'll be connected to a GPT-powered assistant that answers your travel questions in real time — from restaurant recommendations to cultural tips and transportation advice.");

        addFaq(batch, 4, "How do I reset my password?",
            "On the Login screen, tap 'Forgot Password?'. Enter your registered email address and we will send you a secure reset link. Follow the instructions in the email to create a new password.");

        addFaq(batch, 5, "Is Travel SKA free to use?",
            "Yes! Travel SKA is completely free to download and use. All city guides, hotel listings, attraction details, and the AI chat feature are available at no cost.");

        addFaq(batch, 6, "How do I contact support?",
            "For help or feedback, use the Help Desk section in the app to browse frequently asked questions. For direct support, email us at support@travelska.com and our team will respond within 24 hours.");

        batch.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, "FAQs seeded"); next.run(); })
            .addOnFailureListener(e -> Log.e(TAG, "FAQs failed: " + e.getMessage()));
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private static Map<String, Object> city(String name, String desc, String img,
                                             String country, String region, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("description", desc);
        m.put("imageUrl", img);
        m.put("country", country);
        m.put("region", region);
        m.put("latitude", lat);
        m.put("longitude", lng);
        return m;
    }

    private static void addHotel(WriteBatch batch, String name, String cityId, String city,
                                  double rating, String category, String phone, String desc,
                                  String img, double price, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("cityId", cityId);
        m.put("city", city);
        m.put("rating", rating);
        m.put("category", category);
        m.put("phone", phone);
        m.put("description", desc);
        m.put("imageUrl", img);
        m.put("pricePerNight", price);
        m.put("latitude", lat);
        m.put("longitude", lng);
        batch.set(db.collection("hotels").document(), m);
    }

    private static void addAttraction(WriteBatch batch, String name, String cityId, String city,
                                       double rating, String category, String desc,
                                       String img, String type, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("cityId", cityId);
        m.put("city", city);
        m.put("rating", rating);
        m.put("category", category);
        m.put("description", desc);
        m.put("imageUrl", img);
        m.put("type", type);
        m.put("latitude", lat);
        m.put("longitude", lng);
        batch.set(db.collection("attractions").document(), m);
    }

    private static void addActivity(WriteBatch batch, String name, String cityId, String city,
                                     double rating, String category, String desc, String img,
                                     String openTime, String closeTime, String location,
                                     double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("cityId", cityId);
        m.put("city", city);
        m.put("rating", rating);
        m.put("category", category);
        m.put("description", desc);
        m.put("imageUrl", img);
        m.put("openTime", openTime);
        m.put("closeTime", closeTime);
        m.put("location", location);
        m.put("latitude", lat);
        m.put("longitude", lng);
        batch.set(db.collection("activities").document(), m);
    }

    private static void addFaq(WriteBatch batch, int order, String question, String answer) {
        Map<String, Object> m = new HashMap<>();
        m.put("question", question);
        m.put("answer", answer);
        m.put("order", order);
        m.put("isVisible", true);
        batch.set(db.collection("faqs").document(), m);
    }
}
