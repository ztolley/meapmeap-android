package com.exsite.meapmeap.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.exsite.meapmeap.api.Meap
import com.exsite.meapmeap.ui.theme.MeapMeapTheme

@Composable
fun MeapDetailSection(meap: Meap) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    meap.locationName,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(meap.address)
                Text(meap.postcode)


                if (meap.defibrillatorLocation != null) {
                    SectionHeader("Defibrillator Details")
                    Text(meap.defibrillatorLocation)
                    meap.defibrillatorNotes?.let { Text(it) }
                }
                if (meap.stretcherLocation != null) {
                    SectionHeader("Stretcher Details")
                    Text(meap.stretcherLocation)
                }

                if (meap.firstaidRoomLocation != null) {
                    SectionHeader("First Aid Room")
                    Text(meap.firstaidRoomLocation)
                }

                if (meap.hospitalName != null) {
                    SectionHeader("Hospital")
                    Text(
                        meap.hospitalName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    meap.hospitalAddress?.let { Text(it) }
                    meap.hospitalPostcode?.let { Text(it) }
                    meap.hospitalJourneyTime?.let {
                        Text(
                            "Journey time $it",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (meap.walkInCentreName != null) {
                    SectionHeader("Walk-In Centre")
                    Text(
                        meap.walkInCentreName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    meap.walkInCentreAddress?.let { Text(it) }
                    meap.walkInCentrePostcode?.let { Text(it) }
                }


                if (meap.accessRouteForAmbulance != null) {
                    SectionHeader("Access Route for Ambulance")
                    Text(meap.accessRouteForAmbulance)
                }

                SectionHeader("Additional Information")
                meap.what3words?.let { Text("What 3 Words: $it") }
                Text("Updated At: ${meap.updatedAt}")
                Text("Author Name: ${meap.authorName}")
            }
        }

    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp)
    )
}


@Preview(showBackground = true, name = "Text preview", showSystemUi = true)
@Composable
fun MeapDetailSectionPreview() {
    val fakeMeap = Meap(
        id = "762a3361-dd68-49f4-80ab-7a6fde33fda2",
        locationName = "Gordons School Sports Ground",
        address = "Bagshot Road, West End, Woking, Surrey",
        postcode = "GU24 9PT",
        defibrillatorLocation = "On the outside of the cafe building to the side of the building, not opposite the main pitch.",
        defibrillatorNotes = null,
        stretcherLocation = null,
        firstaidRoomLocation = "No first aid room",
        hospitalName = "Frimley Park Hospital",
        hospitalAddress = "Portsmouth Road, Frimley, Surrey",
        hospitalPostcode = "GU16 7UJ",
        hospitalJourneyTime = "15 minutes",
        walkInCentreName = "Farnham Hospital",
        walkInCentreAddress = "Hale Road, Farnham, Surrey",
        walkInCentrePostcode = "GU9 9QL",
        accessRouteForAmbulance = "Sports center opposite side of the main road from the main school building. There is a small road between the two car parks, follow it down a small hill to the sports building that as space for ambulance to park right by the pitch.",
        latitude = "51.345103",
        longitude = "-0.646159",
        what3words = "Green Pink Butterfly",
        createdAt = "2024-05-14T07:58:45.958Z",
        updatedAt = "2024-05-14T07:58:45.958Z",
        authorEmail = "zac@thetolleys.com",
        authorName = "Zac Tolley",
        draft = false
    )

    MeapMeapTheme {
        MeapDetailSection(
            meap = fakeMeap,
        )
    }
}