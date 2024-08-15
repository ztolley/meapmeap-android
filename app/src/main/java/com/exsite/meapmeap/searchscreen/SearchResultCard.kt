package com.exsite.meapmeap.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exsite.meapmeap.api.SearchResult

import java.util.Locale

@Composable
fun SearchResultCard(
    navigateToMeapDetail: (id: String) -> Unit,
    result: SearchResult,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navigateToMeapDetail(result.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = result.locationName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = result.address,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = result.postcode,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Text(
                text = "Distance: ${
                    String.format(
                        Locale.getDefault(),
                        "%.1f",
                        result.distance
                    )
                } km",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
    }
}
