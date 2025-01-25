package com.android.brochurecatalogue.brochureslist.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.android.data.model.domain.Brochure

@Composable
fun BrochureItem(brochure: Brochure, modifier: Modifier = Modifier) {

    // TODO: To move all values to be from constants
    Column(
        modifier = modifier
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = brochure.publisher.name ?: "No Title",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            brochure.brochureImage?.let {
                Box(
                    modifier = Modifier
                        .aspectRatio(0.67f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onBackground)
                ) {

                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(20.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        contentDescription = brochure.publisher.name,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Inside
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, name = "BrochureItem With Image - Light Theme")
@Composable
fun PreviewBrochureItemWithImageLight() {
    val sampleBrochure = Brochure.StandardBrochure(
        id = "1",
        brochureImage = "https://content-media.bonial.biz/10342910-d01c-4959-90b8-1c007395febd/preview.jpg",
        publisher = com.android.data.model.domain.Publisher(
            id = "123",
            name = "Sample Publisher",
        ),
        distance = 5.0
    )

    MaterialTheme {
        BrochureItem(
            brochure = sampleBrochure,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "BrochureItem Without Image - Light Theme")
@Composable
fun PreviewBrochureItemWithoutImageLight() {
    val sampleBrochure = Brochure.StandardBrochure(
        id = "2",
        brochureImage = null,
        publisher = com.android.data.model.domain.Publisher(
            id = "456",
            name = "Another Publisher",
        ),
        distance = 10.0
    )

    MaterialTheme {
        BrochureItem(
            brochure = sampleBrochure,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "BrochureItem With Image - Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBrochureItemWithImageDark() {
    val sampleBrochure = Brochure.StandardBrochure(
        id = "3",
        brochureImage = "https://content-media.bonial.biz/10342910-d01c-4959-90b8-1c007395febd/preview.jpg",
        publisher = com.android.data.model.domain.Publisher(
            id = "789",
            name = "Dark Theme Publisher",
        ),
        distance = 2.0
    )

    MaterialTheme {
        BrochureItem(
            brochure = sampleBrochure,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "BrochureItem Without Image - Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBrochureItemWithoutImageDark() {
    val sampleBrochure = Brochure.StandardBrochure(
        id = "4",
        brochureImage = null,
        publisher = com.android.data.model.domain.Publisher(
            id = "987",
            name = "Dark Mode Publisher",
        ),
        distance = null
    )

    MaterialTheme {
        BrochureItem(
            brochure = sampleBrochure,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
