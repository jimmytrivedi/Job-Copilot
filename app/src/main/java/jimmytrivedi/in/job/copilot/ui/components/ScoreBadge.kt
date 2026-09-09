package jimmytrivedi.`in`.job.copilot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ScoreRed = Color(0xFFD32F2F)
private val ScoreAmber = Color(0xFFF9A825)
private val ScoreGreen = Color(0xFF2E7D32)

fun scoreColor(score: Int): Color = when {
    score < 30 -> ScoreRed
    score <= 65 -> ScoreAmber
    else -> ScoreGreen
}

@Composable
fun ScoreBadge(score: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(120.dp)
            .background(color = scoreColor(score), shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = score.toString(),
            color = Color.White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium,
        )
    }
}
