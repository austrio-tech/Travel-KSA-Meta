package com.example.travelaks;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Seeds Firestore with real-world accurate data for Saudi Arabia travel guide.
 * Uses named document IDs so re-running is idempotent (overwrites, no duplicates).
 */
public class FirebaseDataSeeder {

    private static final String TAG = "FirebaseDataSeeder";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface Callback {
        void onComplete(boolean success, String message);
    }

    public static void seedAll(Callback callback) {
        seedCities(callback, () ->
            seedHotels(callback, () ->
                seedAttractions(callback, () ->
                    seedActivities(callback, () ->
                        seedFaqs(callback, () ->
                            callback.onComplete(true,
                                "All data seeded! Cities, Hotels, Attractions, Activities & FAQs uploaded.")
                        )
                    )
                )
            )
        );
    }

    // ─────────────────────────────────────────────────────────────
    // CITIES
    // ─────────────────────────────────────────────────────────────
    private static void seedCities(Callback cb, Runnable next) {
        WriteBatch b = db.batch();

        b.set(ref("cities", "riyadh"), city(
            "Riyadh",
            "The capital and largest city of Saudi Arabia, a dynamic metropolis blending ultra-modern skyscrapers with deep-rooted Arabian heritage. Home to more than 7.5 million people, Riyadh is the political, financial, and cultural heart of the Kingdom.",
            "riyadh", "Central", 24.7136, 46.6753));

        b.set(ref("cities", "jeddah"), city(
            "Jeddah",
            "Saudi Arabia's gateway to the Red Sea, Jeddah is a cosmopolitan port city celebrated for its UNESCO-listed historic district Al-Balad, the world's tallest fountain, and vibrant waterfront Corniche. It is the Kingdom's commercial capital and most diverse city.",
            "jeddh", "Western", 21.5433, 39.1728));

        b.set(ref("cities", "makkah"), city(
            "Makkah",
            "The holiest city in Islam, birthplace of Prophet Muhammad (PBUH) and home to Masjid Al-Haram — the largest mosque in the world surrounding the sacred Kaaba. Every Muslim who is able must make the Hajj pilgrimage here at least once in their lifetime. Entry is restricted to Muslims only.",
            "makkah", "Hejaz", 21.3891, 39.8579));

        b.set(ref("cities", "madinah"), city(
            "Madinah",
            "The second holiest city in Islam, Madinah is where the Prophet Muhammad (PBUH) migrated and is buried. It is home to Al-Masjid an-Nabawi (the Prophet's Mosque), one of the largest mosques in the world, and a city of profound spiritual significance for 1.9 billion Muslims worldwide.",
            "almadina", "Hejaz", 24.5247, 39.5692));

        commit(b, "Cities", next, cb);
    }

    // ─────────────────────────────────────────────────────────────
    // HOTELS  (5–6 per city, all real properties)
    // ─────────────────────────────────────────────────────────────
    private static void seedHotels(Callback cb, Runnable next) {
        WriteBatch b = db.batch();

        // ── RIYADH ──────────────────────────────────────────────
        hotel(b, "riyadh_four_seasons",
            "Four Seasons Hotel Riyadh at Kingdom Centre",
            "riyadh", "Riyadh", 4.8, "5-Star Luxury",
            "+966 11 211 5000",
            "Occupying the top 30 floors of the iconic Kingdom Centre tower, this Forbes Five-Star hotel offers 274 elegantly appointed rooms and suites with sweeping Riyadh skyline views. Features the award-winning Majlis lounge, an outdoor infinity pool, world-class spa, and direct access to the Kingdom Mall.",
            "four_seasons", 1500, 24.7129, 46.6740);

        hotel(b, "riyadh_ritz_carlton",
            "The Ritz-Carlton, Riyadh",
            "riyadh", "Riyadh", 4.7, "5-Star Palace",
            "+966 11 802 8020",
            "A magnificent palace-hotel set on 52 acres of manicured gardens, modelled after the style of Al-Qasr Palace. Features 492 rooms and suites, 11 restaurants and bars, an indoor pool, an expansive spa, and unparalleled banqueting facilities. One of the most iconic luxury addresses in the Middle East.",
            "ritz_carlton", 1400, 24.6859, 46.7095);

        hotel(b, "riyadh_mandarin_oriental",
            "Mandarin Oriental Al Faisaliah, Riyadh",
            "riyadh", "Riyadh", 4.7, "5-Star Luxury",
            "+966 11 273 2000",
            "Occupying floors 28–41 of the landmark Al Faisaliah Tower in Olaya, the first skyscraper in Saudi Arabia. Offers 163 spacious rooms with panoramic city views, a signature Mandarin Oriental spa, three acclaimed restaurants including the Globe Restaurant inside the tower's iconic glass sphere.",
            "mandarin", 1200, 24.7054, 46.6831);

        hotel(b, "riyadh_fairmont",
            "Fairmont Riyadh",
            "riyadh", "Riyadh", 4.6, "5-Star Luxury",
            "+966 11 249 1000",
            "A contemporary luxury hotel in the heart of Riyadh's Business Gate complex. Features 298 guest rooms and suites with Fairmont Gold floor, four diverse dining venues, a rooftop pool, and a full-service Willow Stream Spa — ideal for business and leisure travellers alike.",
            "riyadh", 1300, 24.7390, 46.6881);

        hotel(b, "riyadh_jw_marriott",
            "JW Marriott Hotel Riyadh",
            "riyadh", "Riyadh", 4.5, "5-Star Business Luxury",
            "+966 11 488 7777",
            "A soaring 66-floor tower on King Fahad Road featuring 349 rooms and suites designed with modern Najdi architectural influences. Boasts six restaurants, a rooftop pool with city views, a state-of-the-art fitness centre, and extensive meeting facilities in the heart of Riyadh's business district.",
            "riyadh", 1100, 24.7550, 46.6890);

        hotel(b, "riyadh_hyatt_regency",
            "Hyatt Regency Riyadh Olaya",
            "riyadh", "Riyadh", 4.4, "5-Star Business",
            "+966 11 288 1234",
            "A sleek contemporary tower in the prestigious Olaya district featuring floor-to-ceiling windows, spacious rooms with stunning city panoramas, five food and beverage outlets, a rooftop pool, and 2,000 sq m of meeting space — a favourite for business travellers and delegations.",
            "hyatt_regency", 950, 24.7202, 46.6819);

        // ── JEDDAH ──────────────────────────────────────────────
        hotel(b, "jeddah_assila",
            "Assila Hotel, a Luxury Collection Hotel, Jeddah",
            "jeddah", "Jeddah", 4.8, "5-Star Art Hotel",
            "+966 12 661 8000",
            "Jeddah's finest art hotel, home to over 2,000 artworks by prominent Saudi and Arab artists. Located on Tahlia Street, the hotel features 195 rooms and suites, three acclaimed restaurants, a rooftop pool, and an immersive art gallery experience — a cultural landmark in its own right.",
            "jeddh", 1600, 21.5344, 39.1597);

        hotel(b, "jeddah_park_hyatt",
            "Park Hyatt Jeddah — Marina & Spa",
            "jeddah", "Jeddah", 4.7, "5-Star Marina Resort",
            "+966 12 653 1234",
            "A stunning marina resort directly on the Red Sea coast, featuring 134 elegantly appointed rooms and villas with private terraces overlooking the marina. Highlights include a 90-metre infinity pool, a world-class spa, the acclaimed Spice Market restaurant, and direct access to a private beach.",
            "jeddh", 1700, 21.5186, 39.1597);

        hotel(b, "jeddah_intercontinental",
            "InterContinental Jeddah",
            "jeddah", "Jeddah", 4.6, "5-Star Landmark",
            "+966 12 229 5555",
            "An iconic 5-star landmark on Al Hamra Corniche overlooking the Red Sea and the King Fahd Fountain. Offers 440 rooms and suites, six restaurants and lounges, an outdoor pool, and a private beach — a prestigious address in Jeddah for over 40 years.",
            "jeddh", 1200, 21.5383, 39.1481);

        hotel(b, "jeddah_sheraton",
            "Sheraton Jeddah Hotel",
            "jeddah", "Jeddah", 4.5, "5-Star",
            "+966 12 699 2212",
            "A well-established luxury hotel on the Northern Corniche with 433 rooms and suites, Red Sea views, five dining outlets, an outdoor pool, and extensive conference facilities covering 4,000 sqm — a reliable choice for both corporate and leisure guests in Jeddah.",
            "jeddh", 1050, 21.5444, 39.1714);

        hotel(b, "jeddah_hilton",
            "Hilton Jeddah",
            "jeddah", "Jeddah", 4.4, "5-Star",
            "+966 12 696 9100",
            "Situated on the Northern Corniche with spectacular Red Sea views, the Hilton Jeddah offers 447 rooms, multiple restaurants including the rooftop Al Bustan, a private beach, outdoor pool, and a health club. Consistently rated as one of Jeddah's most popular hotels for families and business travellers.",
            "jeddh", 900, 21.5439, 39.1736);

        // ── MAKKAH ──────────────────────────────────────────────
        hotel(b, "makkah_fairmont",
            "Makkah Clock Royal Tower, A Fairmont Hotel",
            "makkah", "Makkah", 4.9, "5-Star Ultra-Luxury",
            "+966 12 571 7777",
            "The crown jewel of Makkah's hospitality, located in the iconic Abraj Al-Bait clock tower — the world's third tallest building — directly overlooking Masjid Al-Haram. Offers 858 rooms and suites with Kaaba views, 10 restaurants, the largest hotel ballroom in the Kingdom, and an exclusive Fairmont Gold floor for premium pilgrims.",
            "makkah", 2500, 21.4194, 39.8264);

        hotel(b, "makkah_swissotel",
            "Swissôtel Al Maqam Makkah",
            "makkah", "Makkah", 4.7, "5-Star",
            "+966 12 540 9000",
            "Part of the Abraj Al-Bait complex, the Swissôtel Al Maqam offers 1,577 spacious rooms and suites with unobstructed Grand Mosque views. Features seven dining venues, a health club, meeting facilities, and Swiss-precision service — an excellent base for Umrah and Hajj pilgrims.",
            "makkah", 1800, 21.4186, 39.8233);

        hotel(b, "makkah_raffles",
            "Raffles Makkah Palace",
            "makkah", "Makkah", 4.7, "5-Star Ultra-Luxury",
            "+966 12 577 3333",
            "An exclusive luxury retreat within the Abraj Al-Bait complex offering 213 palatial suites — some of the largest in the world — each with sweeping Haram views. Signature amenities include personal butler service, private Raffles Spa, six bespoke restaurants, and a dedicated Hajj and Umrah concierge.",
            "makkah", 2800, 21.4178, 39.8225);

        hotel(b, "makkah_conrad",
            "Conrad Makkah",
            "makkah", "Makkah", 4.6, "5-Star",
            "+966 12 570 0555",
            "A sleek modern tower in the Jabal Omar development, just steps from Masjid Al-Haram. Features 1,300 stylish rooms with Kaaba views, five restaurants, an indoor pool, and seamless walkway access to the Grand Mosque — combining contemporary luxury with the ultimate in Haram proximity.",
            "makkah", 1600, 21.4236, 39.8270);

        hotel(b, "makkah_pullman_zamzam",
            "Pullman ZamZam Makkah",
            "makkah", "Makkah", 4.5, "5-Star",
            "+966 12 547 8000",
            "Ideally positioned within the Abraj Al-Bait complex with direct Masjid Al-Haram access, the Pullman ZamZam offers 368 rooms and suites, multiple dining venues, a fitness centre, and seamless proximity to the Zamzam well — a preferred choice for pilgrims seeking comfort and convenience.",
            "makkah", 1400, 21.4172, 39.8253);

        // ── MADINAH ──────────────────────────────────────────────
        hotel(b, "madinah_dar_al_taqwa",
            "Dar Al Taqwa Hotel",
            "madinah", "Madinah", 4.7, "5-Star",
            "+966 14 817 7400",
            "One of the closest hotels to Al-Masjid an-Nabawi, located just 50 metres from King Fahd Gate. Offers 218 rooms with direct Mosque views, a luxury restaurant serving local and international cuisine, a fitness centre, and private parking — the ultimate choice for pilgrims prioritising proximity to the Prophet's Mosque.",
            "almadina", 1300, 24.4672, 39.6100);

        hotel(b, "madinah_pullman_zamzam",
            "Pullman ZamZam Madinah",
            "madinah", "Madinah", 4.6, "5-Star",
            "+966 14 825 8787",
            "Adjacent to Al-Masjid an-Nabawi near Al Salam Gate, this 835-room property is one of the largest hotels in Madinah. Features panoramic Mosque views from upper floors, multiple dining options serving international and Arabic cuisine, a full-service spa, and dedicated Umrah concierge services.",
            "almadina", 1200, 24.4692, 39.6120);

        hotel(b, "madinah_movenpick",
            "Anwar Al Madinah Mövenpick Hotel",
            "madinah", "Madinah", 4.5, "5-Star",
            "+966 14 828 5555",
            "A premium Mövenpick property just 2 minutes' walk from Al-Masjid an-Nabawi, featuring 667 rooms with allergy-certified linens, underground parking, EV charging, and direct mall access. The 21st-floor panoramic restaurant offers breathtaking views of the Prophet's Mosque dome and minarets.",
            "almadina", 1100, 24.4680, 39.6113);

        hotel(b, "madinah_sofitel",
            "Sofitel Shahd Al Madinah",
            "madinah", "Madinah", 4.6, "5-Star",
            "+966 14 820 7777",
            "A sophisticated French-inspired luxury hotel a few steps from Al-Masjid an-Nabawi, seamlessly blending local traditions with Sofitel's signature 'Art de Vivre' hospitality. Features 471 rooms and suites, an exquisite French restaurant, patisserie, and elegant lounges offering unrivalled Mosque views.",
            "almadina", 1250, 24.4731, 39.6108);

        hotel(b, "madinah_hilton",
            "Hilton Madinah",
            "madinah", "Madinah", 4.5, "5-Star",
            "+966 14 835 5700",
            "Located near Gate 25 of Masjid Nabawi, just a 3-minute walk from the Prophet's Mosque. Offers 669 comfortable rooms, world-class amenities, multilingual staff experienced in serving pilgrims, a complimentary shuttle to the mosque, and a variety of restaurants serving Saudi and international dishes.",
            "almadina", 1000, 24.4664, 39.6100);

        commit(b, "Hotels", next, cb);
    }

    // ─────────────────────────────────────────────────────────────
    // ATTRACTIONS  (5–6 per city)
    // ─────────────────────────────────────────────────────────────
    private static void seedAttractions(Callback cb, Runnable next) {
        WriteBatch b = db.batch();

        // ── RIYADH ──────────────────────────────────────────────
        attraction(b, "riyadh_at_turaif",
            "At-Turaif District, Diriyah",
            "riyadh", "Riyadh", 4.9, "UNESCO World Heritage Site",
            "The birthplace of the First Saudi State and a UNESCO World Heritage Site since 2010. This 18th-century mud-brick city showcases the finest Najdi architectural tradition, featuring palaces, mosques, and fortifications built from the distinctive local clay. The site played a pivotal role in the political and religious history of the Arabian Peninsula.",
            "turaifddistrict", "historical", 24.7361, 46.5707);

        attraction(b, "riyadh_al_masmak",
            "Al Masmak Fortress",
            "riyadh", "Riyadh", 4.7, "Historic Fortress",
            "Built in 1865 CE, this imposing clay-brick citadel in the heart of old Riyadh was the site of the legendary raid by King Abdulaziz Al-Saud in 1902 that marked the start of the unification of Saudi Arabia. The fortress houses a fascinating museum with weapons, maps, photographs, and artefacts. A spearhead embedded in the main gate from that fateful day remains as a reminder of history.",
            "al_masmak_fort", "historical", 24.6875, 46.7133);

        attraction(b, "riyadh_national_museum",
            "National Museum of Saudi Arabia",
            "riyadh", "Riyadh", 4.6, "National Museum",
            "The flagship cultural institution of the Kingdom, housed in a distinctive crescent-shaped building adjacent to King Abdulaziz Historical Centre. Eight galleries trace the story of the Arabian Peninsula from the Big Bang through prehistoric times, ancient civilisations, the dawn of Islam, and the formation of modern Saudi Arabia — all with state-of-the-art interactive exhibits.",
            "national_museum", "cultural", 24.6884, 46.7127);

        attraction(b, "riyadh_kingdom_centre",
            "Kingdom Centre Tower",
            "riyadh", "Riyadh", 4.5, "Iconic Landmark",
            "Riyadh's most recognisable skyscraper, standing 302 metres tall with its distinctive inverted parabolic arch. Take the high-speed elevator to the Sky Bridge on the 99th floor for a breathtaking 360° panoramic view of the city. The tower also houses the Four Seasons Hotel, the Kingdom Mall with 150+ luxury brands, and a women-only floor in the hotel.",
            "riyadh", "modern", 24.7129, 46.6740);

        attraction(b, "riyadh_edge_of_world",
            "Edge of the World (Jebel Fihrayn)",
            "riyadh", "Riyadh", 4.9, "Natural Wonder",
            "One of Saudi Arabia's most spectacular natural attractions, located approximately 120 km northwest of Riyadh. Standing at the rim of the ancient Tuwaiq escarpment, visitors look out over a sheer 300-metre cliff dropping into an infinite desert horizon — an awe-inspiring geological phenomenon that feels like standing at the end of the earth.",
            "riyadh", "natural", 24.4833, 46.4500);

        attraction(b, "riyadh_diriyah",
            "Diriyah (Wadi Hanifah Oasis)",
            "riyadh", "Riyadh", 4.8, "Heritage & Culture District",
            "The ancient capital of the Al Saud dynasty, undergoing a massive UNESCO-backed restoration transforming it into a world-class cultural destination. Beyond At-Turaif, the wider Diriyah area features beautifully restored mud-brick souqs, art galleries, heritage trails, outdoor dining along Wadi Hanifah, and regular cultural festivals and events.",
            "turaifddistrict", "historical", 24.7344, 46.5717);

        // ── JEDDAH ──────────────────────────────────────────────
        attraction(b, "jeddah_al_balad",
            "Al-Balad Historic District",
            "jeddah", "Jeddah", 4.8, "UNESCO World Heritage Site",
            "Jeddah's extraordinary old city, designated a UNESCO World Heritage Site in 2014. Wander through narrow alleyways lined with centuries-old coral-stone buildings adorned with elaborately carved wooden mashrabiya balconies. The district is home to traditional souqs selling spices, textiles, and incense; historic mosques; and the landmark Nassif House, once home to Jeddah's most prominent merchant family.",
            "jeddh", "historical", 21.4858, 39.1889);

        attraction(b, "jeddah_king_fahd_fountain",
            "King Fahd Fountain",
            "jeddah", "Jeddah", 4.7, "World Record Landmark",
            "The world's tallest fountain, shooting a single jet of seawater 312 metres (1,024 feet) into the sky over the Red Sea — higher than the Eiffel Tower. Visible from 50km away in clear weather, the fountain is illuminated by 500 spotlights at night, creating a dazzling spectacle best appreciated from the Jeddah Corniche. It pumps 630 litres of water per second at 375 km/h.",
            "jeddh", "landmark", 21.5186, 39.1450);

        attraction(b, "jeddah_al_rahma_mosque",
            "Al-Rahma Floating Mosque",
            "jeddah", "Jeddah", 4.7, "Architectural Wonder",
            "One of the most photographed landmarks in the Middle East, this ethereal white mosque appears to float on the surface of the Red Sea during high tide. Built on pillars above the water, the mosque accommodates 1,500 worshippers and is surrounded by the sea on three sides. Its reflection shimmers beautifully on the water, creating an almost magical visual effect.",
            "jeddh", "religious", 21.4703, 39.1575);

        attraction(b, "jeddah_corniche",
            "Jeddah Corniche",
            "jeddah", "Jeddah", 4.5, "Waterfront Promenade",
            "A stunning 30-kilometre coastal promenade stretching along the Red Sea, considered one of the finest waterfronts in the Arab world. Lined with manicured parks, public art installations, cafes, restaurants, cycling paths, and children's playgrounds, the Corniche offers front-row views of the King Fahd Fountain and spectacular Red Sea sunsets. Most vibrant after sunset during Jeddah's warm evenings.",
            "jeddh", "natural", 21.5433, 39.1480);

        attraction(b, "jeddah_nassif_house",
            "Nassif House Museum",
            "jeddah", "Jeddah", 4.5, "Historic Museum",
            "The grandest historic house in Al-Balad, built in 1881 by trader Omar Effendi Nassif. This four-storey coral-stone mansion hosted King Abdulaziz Al-Saud during his first visit to Jeddah and features the city's first public water tap in its courtyard. Now a museum, it preserves traditional Hijazi architecture, furniture, and artefacts offering an intimate glimpse into 19th-century Jeddah merchant life.",
            "jeddh", "historical", 21.4872, 39.1885);

        // ── MAKKAH ──────────────────────────────────────────────
        attraction(b, "makkah_masjid_al_haram",
            "Masjid Al-Haram (The Grand Mosque)",
            "makkah", "Makkah", 5.0, "Holiest Site in Islam",
            "The largest mosque in the world and the holiest site in Islam, encompassing the sacred Kaaba — the black cubic structure draped in hand-embroidered kiswa that Muslims worldwide face during prayer. The mosque complex covers 356,800 square metres and can accommodate over 4 million worshippers during Hajj season. Within the mosque precincts are the Zamzam Well, Maqam Ibrahim, the hills of Safa and Marwa, and the Black Stone. Entry is restricted to Muslims only.",
            "makkah", "religious", 21.4225, 39.8262);

        attraction(b, "makkah_jabal_al_nour",
            "Jabal Al-Nour (Cave of Hira)",
            "makkah", "Makkah", 4.8, "Sacred Religious Site",
            "A sacred mountain northeast of Makkah whose summit contains the Cave of Hira, where the first verses of the Holy Quran were revealed to Prophet Muhammad (PBUH) during Ramadan in 610 CE. A challenging climb of approximately 1,750 steps (about 45 minutes) leads to the small cave where the Prophet spent many months in contemplation. One of the most spiritually charged destinations on earth.",
            "makkah", "religious", 21.4578, 39.8619);

        attraction(b, "makkah_abraj_al_bait",
            "Abraj Al-Bait (Makkah Royal Clock Tower)",
            "makkah", "Makkah", 4.6, "World Record Skyscraper",
            "The Abraj Al-Bait complex is the world's second tallest building at 601 metres and home to the world's largest clock face (43 metres in diameter). This extraordinary complex of seven towers houses luxury hotels including the Fairmont and Raffles, shopping malls, and the Clock Tower Museum dedicated to the science of timekeeping. The clock is visible from 25 kilometres away and the complex can be seen from across Makkah.",
            "makkah", "modern", 21.4187, 39.8229);

        attraction(b, "makkah_jabal_thawr",
            "Jabal Thawr (Cave of Thawr)",
            "makkah", "Makkah", 4.7, "Historic Religious Site",
            "The mountain where Prophet Muhammad (PBUH) and his companion Abu Bakr Al-Siddiq (RA) sought refuge for three nights during the Hijra (migration) to Madinah in 622 CE. The cave at the summit, reached after a 40-minute climb, contains remarkable formations that shielded the Prophet miraculously according to Islamic tradition. A profound historical and spiritual site for all Muslim visitors.",
            "makkah", "religious", 21.3892, 39.8408);

        attraction(b, "makkah_mina_tent_city",
            "Mina — City of Tents",
            "makkah", "Makkah", 4.5, "Hajj Pilgrimage Site",
            "Known as the 'City of Tents', Mina is a sprawling valley on the eastern outskirts of Makkah housing over 160,000 air-conditioned Teflon-coated tents during the Hajj season. The valley encompasses the Jamarat Bridge where the ritual Stoning of the Devil is performed and the grand Masjid Al-Khaif mosque, one of the largest in the world. During Hajj, over 3 million pilgrims stay in Mina for three to four days.",
            "makkah", "religious", 21.4133, 39.8944);

        // ── MADINAH ──────────────────────────────────────────────
        attraction(b, "madinah_masjid_nabawi",
            "Al-Masjid an-Nabawi (Prophet's Mosque)",
            "madinah", "Madinah", 5.0, "Second Holiest Site in Islam",
            "The second holiest mosque in Islam, built by the Prophet Muhammad (PBUH) himself in 622 CE upon his arrival in Madinah. Housing the tomb of the Prophet within the Green Dome, the mosque has been expanded many times and now covers 400,500 square metres with 10 retractable umbrella structures sheltering the outer courtyards. It accommodates over 1 million worshippers and is open 24 hours for prayer throughout the year.",
            "almadina", "religious", 24.4672, 39.6111);

        attraction(b, "madinah_quba_mosque",
            "Quba Mosque",
            "madinah", "Madinah", 4.8, "First Mosque in Islam",
            "The very first mosque built in the history of Islam, founded by the Prophet Muhammad (PBUH) upon his arrival in Madinah in 622 CE. According to Hadith, praying two rak'ahs of nafl salah at Quba Mosque equals the reward of performing a complete Umrah. The current grand white marble structure was rebuilt in 1986 and features gleaming domes, four soaring minarets, and lush courtyard gardens.",
            "almadina", "religious", 24.4400, 39.6178);

        attraction(b, "madinah_mount_uhud",
            "Mount Uhud",
            "madinah", "Madinah", 4.7, "Historic Battlefield",
            "A reddish granite mountain 5 km north of Madinah, site of the famous Battle of Uhud in 625 CE between the Muslim army led by the Prophet and the Quraysh tribe of Makkah. The mountain holds profound religious significance as the burial ground of 70 sahaba (companions) who achieved martyrdom in the battle, including the Prophet's uncle Hamza ibn Abdul-Muttalib (RA). The Prophet reportedly said: 'Uhud is a mountain that loves us and we love it.'",
            "almadina", "historical", 24.5050, 39.6278);

        attraction(b, "madinah_qiblatayn_mosque",
            "Al-Masjid al-Qiblatayn (Mosque of Two Qiblas)",
            "madinah", "Madinah", 4.6, "Historic Mosque",
            "One of the most historically significant mosques in Islam, built in 623 CE. It was here that, during congregational prayer, the divine command arrived to change the direction of prayer (qiblah) from Jerusalem's Al-Aqsa Mosque to the Kaaba in Makkah. The imam turned mid-prayer, and the congregation followed — giving the mosque its unique name. The current structure has two beautifully ornate mihrabs (prayer niches) commemorating this historic event.",
            "almadina", "religious", 24.4819, 39.5903);

        attraction(b, "madinah_al_baqi",
            "Al-Baqi Cemetery (Jannat Al-Baqi)",
            "madinah", "Madinah", 4.5, "Sacred Cemetery",
            "The oldest and most sacred Islamic cemetery, located adjacent to Al-Masjid an-Nabawi. The burial ground of thousands of the Prophet's companions, family members (including several of his wives and daughter Fatimah), and early Muslims. Visiting Al-Baqi and reciting prayers for its occupants is a revered Sunnah practice for pilgrims to Madinah. Open for visits twice daily — after Fajr and after Asr prayers.",
            "almadina", "religious", 24.4664, 39.6144);

        commit(b, "Attractions", next, cb);
    }

    // ─────────────────────────────────────────────────────────────
    // ACTIVITIES  (5–6 per city)
    // ─────────────────────────────────────────────────────────────
    private static void seedActivities(Callback cb, Runnable next) {
        WriteBatch b = db.batch();

        // ── RIYADH ──────────────────────────────────────────────
        activity(b, "riyadh_boulevard_city",
            "Boulevard Riyadh City",
            "riyadh", "Riyadh", 4.8, "Mega Entertainment Destination",
            "Saudi Arabia's largest entertainment destination spanning 430,000 square metres and divided into nine themed zones. Experience a replica of New York's Times Square, a dancing fountain show, an outdoor concert arena, international restaurants, family rides, shopping, and live performances. Boulevard Riyadh City has been a centrepiece of Saudi Vision 2030's entertainment initiatives since opening in 2019.",
            "boulevard_world", "4 PM", "12 AM", "King Salman Road, Al-Aqiq District, North Riyadh", 24.8081, 46.7372);

        activity(b, "riyadh_desert_safari",
            "Red Sand Dunes Desert Safari",
            "riyadh", "Riyadh", 4.9, "Outdoor Adventure",
            "Experience the raw beauty of the Arabian Desert at the famous Al-Thumamah Red Sand Dunes, just 60 km from central Riyadh. Choose from exhilarating dune bashing in 4WDs, sandboarding down towering dunes, camel rides at sunset, quad biking, and authentic Bedouin camp experiences with Arabic coffee, dates, and a traditional BBQ dinner under a star-filled desert sky.",
            "riyadh", "7 AM", "9 PM", "Al-Thumamah Road, 60 km from Riyadh", 24.5917, 46.9667);

        activity(b, "riyadh_diriyah_season",
            "Diriyah Season & Arts Festival",
            "riyadh", "Riyadh", 4.7, "Cultural Festival",
            "The annual Diriyah Season transforms the UNESCO-listed At-Turaif district into a world-class entertainment and cultural festival. Features Formula E races on the streets of Diriyah, open-air concerts, international art exhibitions, Saudi cultural performances, luxury dining in restored mud-brick settings, and the prestigious Diriyah Tennis Cup. One of the most unique event experiences in the Middle East.",
            "turaifddistrict", "4 PM", "12 AM", "Diriyah, Northwest Riyadh", 24.7344, 46.5717);

        activity(b, "riyadh_kingdom_sky_bridge",
            "Kingdom Centre Sky Bridge Experience",
            "riyadh", "Riyadh", 4.5, "Panoramic Observation",
            "Ascend to the famous Sky Bridge on the 99th floor of Kingdom Centre Tower via high-speed elevator for a breathtaking 360° panoramic view of Riyadh's vast skyline extending to the desert horizon. The suspended glass bridge between the tower's twin pinnacles offers an unmatched vantage point, especially magical during the golden hour before sunset when the city glows.",
            "riyadh", "9 AM", "12 AM", "Kingdom Centre Tower, Olaya Street", 24.7129, 46.6740);

        activity(b, "riyadh_riyadh_zoo",
            "Riyadh Zoo",
            "riyadh", "Riyadh", 4.2, "Family Attraction",
            "The largest zoo in the Arabian Peninsula, covering over 215 hectares in the Al Malaz district and home to more than 1,500 animals representing nearly 200 species. Highlights include the Croc Cave, Kangaroo Arena, Elephant Savannah, and a bird sanctuary. An internal train ride lets you cover the vast grounds comfortably — perfect for a full family day out.",
            "riyadh_zoo", "8 AM", "4:30 PM", "Al Malaz District, Riyadh", 24.6856, 46.7367);

        activity(b, "riyadh_snow_city",
            "Snow City Riyadh",
            "riyadh", "Riyadh", 4.0, "Indoor Winter Experience",
            "Beat the Arabian heat at this year-round indoor winter wonderland inside Al Othaim Mall, featuring real snow, ice skating, mini ski slopes, tubing runs, and snow play areas maintained at -2°C. Saudi Arabia's only indoor snow park is especially popular with children who may never have seen snow before — a genuinely unique experience in the heart of the desert.",
            "snow_city", "10 AM", "12 AM", "Al Othaim Mall, Al Rabwah District, Riyadh", 24.7683, 46.6767);

        // ── JEDDAH ──────────────────────────────────────────────
        activity(b, "jeddah_red_sea_diving",
            "Red Sea Scuba Diving & Snorkelling",
            "jeddah", "Jeddah", 4.9, "Water Sport",
            "Dive into one of the world's richest and most pristine coral reef ecosystems — the Red Sea. The waters off Jeddah's coast offer visibility up to 30 metres, vibrant coral gardens, and extraordinary marine biodiversity including reef sharks, eagle rays, sea turtles, and hundreds of fish species. PADI-certified dive centres offer courses and guided dives for all levels, from complete beginners to advanced divers.",
            "jeddh", "7 AM", "5 PM", "South Corniche Dive Clubs, Jeddah", 21.4925, 39.0800);

        activity(b, "jeddah_al_shallal",
            "Al-Shallal Theme Park",
            "jeddah", "Jeddah", 4.6, "Theme Park & Entertainment",
            "One of the largest and most popular theme parks in the Middle East, located on the Red Sea coast. Features over 30 thrill rides including the spectacular 240-foot Super Shot free-fall tower, a 5-D cinema, an Olympic-sized ice skating rink, a bowling alley, a mini-golf course, an indoor go-kart track, and a waterpark section — providing a full day of entertainment for the entire family.",
            "jeddh", "3 PM", "12 AM", "Al Rawdah District, Corniche Road, North Jeddah", 21.5861, 39.1300);

        activity(b, "jeddah_fakieh_aquarium",
            "Fakieh Aquarium & Dolphin Show",
            "jeddah", "Jeddah", 4.5, "Marine Attraction",
            "Jeddah's premier marine attraction featuring three distinct experiences: an indoor aquarium displaying 200+ Red Sea fish species including sharks, rays, and sea turtles; professional dolphin and sea lion show performances; and an outdoor waterpark with pools and slides. A must-visit for families and ocean enthusiasts, with interactive touch tanks and educational presentations by marine biologists.",
            "jeddh", "10 AM", "10 PM", "Al Shati District, North Corniche, Jeddah", 21.5439, 39.1250);

        activity(b, "jeddah_al_balad_night_tour",
            "Al-Balad Night Walking Tour",
            "jeddah", "Jeddah", 4.7, "Cultural Heritage Tour",
            "The magic of Jeddah's UNESCO-listed old city truly comes alive after sunset when lanterns illuminate the ancient coral-stone alleyways and the souqs overflow with life. Explore centuries-old architectural gems, visit traditional spice and incense markets, sample authentic Hijazi street food, meet local artisans in their workshops, and hear the fascinating stories behind the historic merchant families of old Jeddah.",
            "jeddh", "5 PM", "11 PM", "Al-Balad Historic District, Old Jeddah", 21.4858, 39.1889);

        activity(b, "jeddah_corniche_walk",
            "Jeddah Corniche Sunset & Fountain Walk",
            "jeddah", "Jeddah", 4.5, "Leisure & Recreation",
            "A leisurely stroll or bicycle ride along the magnificent 30-km Jeddah Corniche at sunset is one of the city's most iconic experiences. Time your walk to watch the King Fahd Fountain light up as dusk falls — the world's tallest fountain illuminated by 500 coloured spotlights creates a breathtaking display over the Red Sea. Street cafes, juice stalls, and food carts line the promenade.",
            "jeddh", "4 PM", "12 AM", "Jeddah Corniche, Along the Red Sea", 21.5433, 39.1480);

        // ── MAKKAH ──────────────────────────────────────────────
        activity(b, "makkah_observation_deck",
            "Abraj Al-Bait Observation Deck",
            "makkah", "Makkah", 4.5, "Panoramic City View",
            "Ascend to the observation deck of the Makkah Clock Royal Tower — the world's second tallest building — for an extraordinary bird's-eye view of Islam's holiest city. On a clear day, you can see Masjid Al-Haram directly below, the tent city of Mina in the distance, Jabal Al-Nour and Jabal Thawr mountains on the horizon, and the vast city of Makkah extending to the desert.",
            "makkah", "10 AM", "11 PM", "Abraj Al-Bait Towers, Central Makkah", 21.4187, 39.8229);

        activity(b, "makkah_souq_al_zahed",
            "Souq Al-Zahed & Traditional Markets",
            "makkah", "Makkah", 4.4, "Traditional Shopping",
            "Makkah's most famous and vibrant traditional market district, operating near the Grand Mosque for centuries. Browse hundreds of stalls selling Islamic books and prayer items, prayer beads (masbaha) in precious stones, miswak sticks, attar (pure perfume oils), luxurious oud and bakhoor incense, Zamzam water containers, authentic ihram garments, and beautiful Islamic calligraphy gifts and keepsakes.",
            "makkah", "9 AM", "2 AM", "Near Masjid Al-Haram, Central Makkah", 21.4167, 39.8194);

        activity(b, "makkah_kiswa_factory",
            "Kiswa Factory Tour",
            "makkah", "Makkah", 4.6, "Cultural & Educational",
            "Visit the King Abdulaziz Complex for the Kiswa — the factory where the sacred black cloth (kiswa) covering the Holy Kaaba is crafted each year. Witness skilled artisans weaving pure black silk, embroidering verses from the Holy Quran in gold and silver thread using traditional hand-weaving looms. The factory produces 670 kg of pure silk kiswa annually, requiring 240 craftsmen and 8 months of work. A deeply moving insight into Islamic craftsmanship.",
            "makkah", "8 AM", "12 PM", "Ajyad District, near Masjid Al-Haram", 21.4094, 39.8481);

        // ── MADINAH ──────────────────────────────────────────────
        activity(b, "madinah_date_market",
            "Madinah Date Market Tour",
            "madinah", "Madinah", 4.7, "Cultural & Culinary Experience",
            "Madinah is world-renowned for producing the finest dates (tamr) in the Kingdom, and visiting its legendary date markets is a pilgrimage in itself. Explore row upon row of stalls displaying over 100 premium varieties — from the prized Ajwa dates mentioned in Hadith, to Medjool, Safawi, Mabroom, and Sukkari. Vendors offer unlimited free tastings, and you can purchase beautifully gift-boxed selections to take home.",
            "almadina", "8 AM", "10 PM", "Central Market, Al-Munawwarah Road, Madinah", 24.4697, 39.6100);

        activity(b, "madinah_mount_uhud_tour",
            "Mount Uhud Guided Historical Tour",
            "madinah", "Madinah", 4.7, "Historical & Educational Tour",
            "A deeply moving guided tour of the Battle of Uhud site with expert historians providing rich commentary on the events of 625 CE. Visit the Martyrs' Hill (Jebel Ar-Rumat/Archers' Hill), the grave of Hamza ibn Abdul-Muttalib (RA), walk the battlefield, and understand the profound lessons from this pivotal moment in early Islamic history. The Prophet Muhammad (PBUH) is reported to have said: 'Uhud loves us and we love it.'",
            "almadina", "7 AM", "6 PM", "Uhud Mountain, 5 km North of Madinah", 24.5050, 39.6278);

        activity(b, "madinah_quba_mosque_visit",
            "Quba Mosque Prayer Visit",
            "madinah", "Madinah", 4.8, "Spiritual Experience",
            "Praying two rak'ahs of Nafl Salah at Quba Mosque carries a reward equivalent to performing a complete Umrah — making this visit one of the most spiritually rewarding activities in all of Madinah. The Prophet Muhammad (PBUH) would visit Quba every Saturday to pray. The recently expanded grand white mosque features stunning architecture, serene courtyards, and an atmosphere of deep spiritual tranquillity.",
            "almadina", "5 AM", "11 PM", "Quba District, 3.5 km Southwest of Prophet's Mosque", 24.4400, 39.6178);

        activity(b, "madinah_hejaz_railway_museum",
            "Al-Hejaz Railway Museum",
            "madinah", "Madinah", 4.5, "Historic Museum",
            "Housed in Madinah's beautifully preserved original Ottoman-era Hejaz Railway station, this museum takes visitors on a fascinating journey through one of history's most ambitious engineering projects. Explore original 1908 locomotives, passenger carriages, and freight wagons; study rare maps, photographs, and documents from the Ottoman period; and learn about the dramatic role the railway played in the Arab Revolt of 1916 — immortalised in the story of Lawrence of Arabia.",
            "almadina", "9 AM", "5 PM", "Old Train Station, Al-Anbariyya District, Madinah", 24.4708, 39.5861);

        commit(b, "Activities", next, cb);
    }

    // ─────────────────────────────────────────────────────────────
    // FAQS
    // ─────────────────────────────────────────────────────────────
    private static void seedFaqs(Callback cb, Runnable next) {
        WriteBatch b = db.batch();

        faq(b, "faq_what_is",   1,
            "What is Travel SKA?",
            "Travel SKA is an AI-powered travel companion app for Saudi Arabia (KSA). It helps you explore top cities including Riyadh, Jeddah, Makkah, and Madinah — discover real hotels, tourist attractions, and activities, and get instant personalised answers from our built-in AI travel assistant powered by GPT. Whether you're planning Umrah, Hajj, or a leisure trip, Travel SKA has you covered.");

        faq(b, "faq_cities",    2,
            "Which cities are currently covered in the app?",
            "Travel SKA currently covers four major Saudi Arabian cities: Riyadh (the capital), Jeddah (the Red Sea coastal hub), Makkah (the holiest city in Islam — accessible to Muslims only), and Madinah (home of Al-Masjid an-Nabawi). Each city section includes hotels, tourist attractions, activities, and an AI chat assistant. More cities are being added regularly.");

        faq(b, "faq_ai_chat",   3,
            "How does the AI Chat feature work?",
            "Select any city and tap the 'Ask AI' button to open the AI travel assistant. You can ask anything about that city — from hotel recommendations and restaurant suggestions to prayer times, cultural tips, best transport options, and hidden gems. The assistant is powered by OpenAI's GPT technology and responds in real time. All conversations are saved so you can refer back to previous advice.");

        faq(b, "faq_makkah",    4,
            "Can non-Muslims visit Makkah through the app?",
            "Entry to the city of Makkah is restricted exclusively to Muslims — this is a religious requirement enforced by Saudi law. The Makkah section of Travel SKA is designed for Muslim visitors planning Umrah, Hajj, or general pilgrimage. Non-Muslim travellers can explore Riyadh, Jeddah, and Madinah fully; Jeddah in particular is an excellent base for non-Muslim tourists visiting the region.");

        faq(b, "faq_reset",     5,
            "How do I reset my password?",
            "On the Login screen, tap 'Forgot Password?' and enter your registered email address. Travel SKA will immediately send you a secure password reset link to that email. Click the link in your inbox (check your spam folder too), and you will be directed to a secure page to set a new password. The link expires after 24 hours for your security.");

        faq(b, "faq_free",      6,
            "Is Travel SKA free to use?",
            "Yes! Travel SKA is completely free to download and use. All city guides, hotel listings, attraction details, activity recommendations, and AI chat sessions are available at no cost. There are no in-app purchases or subscription fees. Simply sign up with your email and start exploring Saudi Arabia instantly.");

        commit(b, "FAQs", next, cb);
    }

    // ─────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────
    private static com.google.firebase.firestore.DocumentReference ref(String col, String id) {
        return db.collection(col).document(id);
    }

    private static Map<String, Object> city(String name, String desc, String img,
                                             String region, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);        m.put("description", desc);
        m.put("imageUrl", img);     m.put("country", "Saudi Arabia");
        m.put("region", region);    m.put("latitude", lat);
        m.put("longitude", lng);
        return m;
    }

    private static void hotel(WriteBatch b, String id, String name, String cityId, String city,
                               double rating, String category, String phone, String desc,
                               String img, double price, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);         m.put("cityId", cityId);    m.put("city", city);
        m.put("rating", rating);     m.put("category", category); m.put("phone", phone);
        m.put("description", desc);  m.put("imageUrl", img);     m.put("pricePerNight", price);
        m.put("latitude", lat);      m.put("longitude", lng);
        b.set(ref("hotels", id), m);
    }

    private static void attraction(WriteBatch b, String id, String name, String cityId, String city,
                                    double rating, String category, String desc,
                                    String img, String type, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);         m.put("cityId", cityId);    m.put("city", city);
        m.put("rating", rating);     m.put("category", category); m.put("description", desc);
        m.put("imageUrl", img);      m.put("type", type);         m.put("latitude", lat);
        m.put("longitude", lng);
        b.set(ref("attractions", id), m);
    }

    private static void activity(WriteBatch b, String id, String name, String cityId, String city,
                                  double rating, String category, String desc, String img,
                                  String open, String close, String location, double lat, double lng) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);         m.put("cityId", cityId);    m.put("city", city);
        m.put("rating", rating);     m.put("category", category); m.put("description", desc);
        m.put("imageUrl", img);      m.put("openTime", open);    m.put("closeTime", close);
        m.put("location", location); m.put("latitude", lat);     m.put("longitude", lng);
        b.set(ref("activities", id), m);
    }

    private static void faq(WriteBatch b, String id, int order, String question, String answer) {
        Map<String, Object> m = new HashMap<>();
        m.put("question", question); m.put("answer", answer);
        m.put("order", order);       m.put("isVisible", true);
        b.set(ref("faqs", id), m);
    }

    private static void commit(WriteBatch b, String label, Runnable next, Callback cb) {
        b.commit()
            .addOnSuccessListener(v -> { Log.d(TAG, label + " seeded OK"); next.run(); })
            .addOnFailureListener(e -> {
                Log.e(TAG, label + " failed: " + e.getMessage());
                cb.onComplete(false, label + " failed: " + e.getMessage());
            });
    }
}
